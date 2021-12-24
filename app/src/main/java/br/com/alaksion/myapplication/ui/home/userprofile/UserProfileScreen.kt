package br.com.alaksion.myapplication.ui.home.userprofile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.components.TryAgain
import br.com.alaksion.core_ui.components.loaders.ProgressIndicator
import br.com.alaksion.core_ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.common.extensions.safeFlowCollect
import br.com.alaksion.myapplication.domain.model.AuthorPhotos
import br.com.alaksion.myapplication.domain.model.Author
import br.com.alaksion.myapplication.ui.components.userdetails.UserDetailsInfo
import br.com.alaksion.myapplication.ui.components.userdetails.header.UserDetailsHeader
import br.com.alaksion.myapplication.ui.home.authordetails.components.authorphotos.AuthorPhotosList
import br.com.alaksion.myapplication.ui.model.ViewState
import kotlinx.coroutines.flow.collect

@ExperimentalFoundationApi
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel,
    popBackStack: () -> Boolean,
    authorUsername: String,
    navigateToPhotoViewer: (photoUrl: String) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = authorUsername, key2 = viewModel, key3 = lifecycleOwner) {
        viewModel.getUserProfileData(authorUsername)

        safeFlowCollect(lifecycleOwner) {
            viewModel.eventHandler.collect { event ->
                when (event) {
                    is UserProfileEvents.ShowMorePhotosError -> {

                    }
                }
            }
        }
    }

    UserProfileContent(
        userData = viewModel.userData.collectAsState().value,
        username = authorUsername,
        userPhotos = viewModel.userPhotos.toList(),
        userPhotosState = viewModel.userPhotosState.collectAsState().value,
        popBackStack = popBackStack,
        getMorePhotos = { viewModel.getMoreUserPhotos() },
        tryAgainGetUserData = { viewModel.getUserProfileData(authorUsername) },
        navigateToPhotoViewer = { photoUrl ->
            navigateToPhotoViewer(photoUrl)
        },
    )
}

@ExperimentalFoundationApi
@Composable
internal fun UserProfileContent(
    userData: ViewState<Author>,
    username: String,
    userPhotos: List<AuthorPhotos>,
    userPhotosState: ViewState<Unit>,
    popBackStack: () -> Boolean,
    getMorePhotos: () -> Unit,
    isPreview: Boolean = false,
    tryAgainGetUserData: () -> Unit,
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
                    username, style = MaterialTheme.typography.body1.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colors.onBackground
                    )
                )
            }
        }
        when (userData) {
            is ViewState.Loading, is ViewState.Idle -> UserProfileLoading()
            is ViewState.Ready -> UserProfileReady(
                authorData = userData.data,
                authorPhotos = userPhotos,
                getMorePhotos = getMorePhotos,
                navigateToPhotoViewer = navigateToPhotoViewer,
                authorPhotoState = userPhotosState,
                isPreview = isPreview
            )
            is ViewState.Error -> UserProfileError { tryAgainGetUserData() }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun UserProfileReady(
    authorData: Author,
    authorPhotos: List<AuthorPhotos>,
    authorPhotoState: ViewState<Unit>,
    getMorePhotos: () -> Unit,
    isPreview: Boolean = false,
    navigateToPhotoViewer: (photoUrl: String) -> Unit
) {
    val horizontalPadding = 10.dp

    Column {
        UserDetailsHeader(
            profileImageUrl = authorData.profileImage,
            photoCount = authorData.totalPhotos,
            followersCount = authorData.followers,
            followingCount = authorData.following,
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(bottom = 10.dp),
            isPreview = isPreview,
        )
        UserDetailsInfo(
            bio = authorData.bio,
            name = authorData.name,
            portfolioUrl = authorData.portfolioUrl,
            twitterUser = authorData.twitterUser,
            instagramUser = authorData.instagramUser,
            modifier = Modifier
                .padding()
                .padding(horizontal = horizontalPadding)
        )
        OutlinedButton(
            onClick = { },
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.onBackground,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding()
                .padding(vertical = 10.dp, horizontal = horizontalPadding)
        ) {
            Text(
                "Edit profile",
                style = MaterialTheme.typography.body2
                    .copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.background)
            )
        }
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
fun UserProfileError(
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
fun UserProfileLoading() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    ProgressIndicator()
}

@ExperimentalFoundationApi
@Composable
@Preview(showBackground = true)
fun UserProfilePreview() {
    ImagefyTheme(true) {
        Scaffold() {
            UserProfileContent(
                userData = ViewState.Ready(
                    Author(
                        username = "SuperJohn",
                        name = "John Doe",
                        profileImage = "",
                        bio = "I'm an amateur photographer",
                        totalLikes = 100,
                        totalPhotos = 20,
                        instagramUser = "johndoe.insta",
                        twitterUser = "johndoe.twitter",
                        portfolioUrl = "portfolio.com.br",
                        followers = 140,
                        following = 200,
                        followedByUser = false
                    )
                ),
                getMorePhotos = {},
                popBackStack = { true },
                navigateToPhotoViewer = {},
                tryAgainGetUserData = {},
                isPreview = true,
                username = "JohnDoe",
                userPhotosState = ViewState.Ready(Unit),
                userPhotos = listOf()
            )
        }
    }
}
