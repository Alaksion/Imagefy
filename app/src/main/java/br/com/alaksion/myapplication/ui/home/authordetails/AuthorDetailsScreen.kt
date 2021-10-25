package br.com.alaksion.myapplication.ui.home.authordetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.extensions.invert
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.components.userdetails.UserDetailsInfo
import br.com.alaksion.myapplication.ui.components.userdetails.header.UserDetailsHeader
import br.com.alaksion.myapplication.ui.home.authordetails.components.authorphotos.AuthorPhotosList
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme

const val AUTHOR_USERNAME_ARG = "author_username"

@ExperimentalFoundationApi
@Composable
fun AuthorDetailsScreen(
    viewModel: AuthorDetailsViewModel,
    popBackStack: () -> Boolean,
    authorUsername: String,
    navigateToPhotoViewer: (photoUrl: String) -> Unit,
    shouldShowBottomBar: MutableState<Boolean>
) {

    LaunchedEffect(null) {
        viewModel.getAuthorProfileData(authorUsername)
        shouldShowBottomBar.value = false
    }

    AuthorDetailsScreenContent(
        authorData = viewModel.authorData.value,
        authorUsername = authorUsername,
        authorPhotos = viewModel.authorPhotos.toList(),
        authorPhotoState = viewModel.authorPhotosState.value,
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
    authorData: ViewState<AuthorResponse>,
    authorUsername: String,
    authorPhotos: List<AuthorPhotosResponse>,
    authorPhotoState: ViewState<Unit>,
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
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Black)
                )
            }
        }
        when (authorData) {
            is ViewState.Loading, is ViewState.Idle -> AuthorDetailsLoading()
            is ViewState.Ready -> AuthorDetailsReady(
                authorData = authorData.data,
                authorPhotos = authorPhotos,
                getMorePhotos = getMorePhotos,
                navigateToPhotoViewer = navigateToPhotoViewer,
                authorPhotoState = authorPhotoState,
                isPreview = isPreview,
                onFollowClick = {}
            )
            is ViewState.Error -> AuthorDetailsError { tryAgainGetAuthorData() }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun AuthorDetailsReady(
    isPreview: Boolean,
    authorData: AuthorResponse,
    authorPhotos: List<AuthorPhotosResponse>,
    authorPhotoState: ViewState<Unit>,
    onFollowClick: (isFollowing: Boolean) -> Unit,
    getMorePhotos: () -> Unit,
    navigateToPhotoViewer: (photoUrl: String) -> Unit
) {
    val isFollowedByUser = remember { mutableStateOf(true) }

    Column {
        UserDetailsHeader(
            profileImageUrl = authorData.profileImage,
            photoCount = authorData.totalPhotos,
            followersCount = authorData.followers,
            followingCount = authorData.following,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 10.dp),
            isPreview = isPreview,
            showFollowButton = true,
            isFollowedByUser = isFollowedByUser.value,
            onFollowClick = {
                onFollowClick(isFollowedByUser.value)
                isFollowedByUser.invert()
            }
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
        AuthorPhotosList(
            photos = authorPhotos,
            onClickTryAgain = { },
            navigateToPhotoViewer = navigateToPhotoViewer,
            viewState = authorPhotoState,
            isPreview = isPreview
        ) { getMorePhotos() }
    }
}

@Composable
fun AuthorDetailsError(
    onTryAgain: () -> Unit
) {
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

@ExperimentalFoundationApi
@Composable
@Preview(showBackground = true)
fun AuthorDetailsPreview() {
    ImagefyTheme {
        AuthorDetailsScreenContent(
            authorData = ViewState.Ready(
                AuthorResponse(
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
                    profileImage = ""
                )
            ),
            authorUsername = "JohnDoe",
            authorPhotos = listOf(),
            authorPhotoState = ViewState.Ready(Unit),
            popBackStack = { true },
            getMorePhotos = { },
            tryAgainGetAuthorData = { },
            navigateToPhotoViewer = {},
            isPreview = true
        )
    }
}
