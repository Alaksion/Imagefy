package br.com.alaksion.myapplication.ui.home.authordetails

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.components.tryagain.TryAgain
import br.com.alaksion.core_ui.components.loaders.ProgressIndicator
import br.com.alaksion.core_ui.theme.DimGray
import br.com.alaksion.core_ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.domain.model.Author
import br.com.alaksion.myapplication.domain.model.AuthorPhotos
import br.com.alaksion.myapplication.ui.home.authordetails.components.UserDetailsInfo
import br.com.alaksion.myapplication.ui.home.authordetails.components.header.UserDetailsHeader
import br.com.alaksion.myapplication.ui.home.authordetails.components.AuthorPhotoGrid
import br.com.alaksion.myapplication.ui.model.ViewState
import kotlinx.coroutines.flow.collect

const val AUTHOR_USERNAME_ARG = "author_username"

@ExperimentalFoundationApi
@Composable
fun AuthorDetailsScreen(
    viewModel: AuthorDetailsViewModel,
    popBackStack: () -> Boolean,
    authorUsername: String,
    navigateToPhotoViewer: (photoUrl: String) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(
        key1 = lifecycleOwner,
        key2 = viewModel,
        key3 = authorUsername
    ) {
        viewModel.getAuthorProfileData(authorUsername)

        viewModel.events.collect { event ->
            when (event) {
                is AuthorDetailsEvents.ShowErrorToast -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.author_details_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    AuthorDetailsScreenContent(
        authorData = viewModel.authorData.collectAsState().value,
        authorUsername = authorUsername,
        authorPhotoState = viewModel.authorPhotosState.collectAsState().value,
        popBackStack = popBackStack,
        getMorePhotos = { viewModel.getMoreAuthorPhotos() },
        tryAgainGetAuthorData = { viewModel.getAuthorProfileData(authorUsername) },
        navigateToPhotoViewer = { photoUrl ->
            navigateToPhotoViewer(photoUrl)
        }
    )

}

@ExperimentalFoundationApi
@Composable
internal fun AuthorDetailsScreenContent(
    isPreview: Boolean = false,
    authorData: ViewState<Author>,
    authorUsername: String,
    authorPhotoState: ViewState<List<AuthorPhotos>>,
    popBackStack: () -> Boolean,
    getMorePhotos: () -> Unit,
    tryAgainGetAuthorData: () -> Unit,
    navigateToPhotoViewer: (photoUrl: String) -> Unit,
) {
    Column {
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { popBackStack() },
                    modifier = Modifier
                        .size(48.dp)
                        .padding()
                        .padding(end = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground,
                    )
                }
                Text(
                    authorUsername,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colors.onBackground
                    )
                )
            }
        }
        when (authorData) {
            is ViewState.Loading, is ViewState.Idle -> AuthorDetailsLoading()
            is ViewState.Ready -> AuthorDetailsReady(
                authorData = authorData.data,
                getMorePhotos = getMorePhotos,
                navigateToPhotoViewer = navigateToPhotoViewer,
                authorPhotoState = authorPhotoState,
                isPreview = isPreview,
            )
            is ViewState.Error -> AuthorDetailsError { tryAgainGetAuthorData() }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun AuthorDetailsReady(
    isPreview: Boolean,
    authorData: Author,
    authorPhotoState: ViewState<List<AuthorPhotos>>,
    getMorePhotos: () -> Unit,
    navigateToPhotoViewer: (photoUrl: String) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        item {
            UserDetailsHeader(
                profileImageUrl = authorData.profileImage,
                photoCount = authorData.totalPhotos,
                followersCount = authorData.followers,
                followingCount = authorData.following,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp),
                isPreview = isPreview
            )
            UserDetailsInfo(
                bio = authorData.bio,
                name = authorData.name,
                portfolioUrl = authorData.portfolioUrl,
                twitterUser = authorData.twitterUser,
                instagramUser = authorData.instagramUser,
                modifier = Modifier
                    .padding()
                    .padding(horizontal = 10.dp)
            )
            if (authorData.followedByUser) {
                FollowButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 10.dp)
                        .width(150.dp)
                )
            }
        }
        when (authorPhotoState) {
            is ViewState.Ready -> {
                AuthorPhotoGrid(
                    listState = listState,
                    authorPhotos = authorPhotoState.data,
                    getMorePhotos = getMorePhotos,
                    navigateToPhotoViewer = navigateToPhotoViewer
                )
            }
            is ViewState.Loading, is ViewState.Idle -> item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProgressIndicator()
                }
            }
            is ViewState.Error -> item {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    TryAgain(
                        message = "An error occurred and author data could not be loaded, please try again later",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Report,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        onClick = { },
                        modifier = Modifier.padding(horizontal = 40.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AuthorDetailsError(
    onTryAgain: () -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        TryAgain(
            message = stringResource(id = R.string.author_details_error),
            icon = {
                Icon(
                    imageVector = Icons.Default.Report,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(40.dp)
                )
            },
            onClick = { onTryAgain() },
            modifier = Modifier.padding(horizontal = 40.dp)
        )
    }
}

@Composable
fun AuthorDetailsLoading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ProgressIndicator()
    }
}

@Composable
fun FollowButton(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.following),
        style = MaterialTheme.typography.caption.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center
        ),
        modifier = modifier
            .border(
                1.dp,
                DimGray,
                RoundedCornerShape(8.dp)
            )
            .padding(vertical = 10.dp)
    )
}

@ExperimentalFoundationApi
@Composable
@Preview(showBackground = true)
fun AuthorDetailsPreview() {
    ImagefyTheme(false) {
        Scaffold() {
            AuthorDetailsScreenContent(
                authorData = ViewState.Ready(
                    Author(
                        name = "John Doe",
                        portfolioUrl = "portfolio",
                        twitterUser = "@john_doe",
                        instagramUser = "@john_doe",
                        username = "JohnDoe",
                        following = 100,
                        followers = 200,
                        totalPhotos = 150,
                        totalLikes = 1,
                        bio = "This is my bio",
                        profileImage = "",
                        followedByUser = true
                    )
                ),
                authorUsername = "JohnDoe",
                authorPhotoState = ViewState.Ready(listOf()),
                popBackStack = { true },
                getMorePhotos = { },
                tryAgainGetAuthorData = { },
                navigateToPhotoViewer = {},
                isPreview = true
            )
        }
    }
}
