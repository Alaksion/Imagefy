package br.com.alaksion.myapplication.ui.home.userprofile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.components.userdetails.UserDetailsHeader
import br.com.alaksion.myapplication.ui.components.userdetails.UserDetailsInfo
import br.com.alaksion.myapplication.ui.home.authordetails.components.authorphotos.AuthorPhotosList
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.MyApplicationTheme

@ExperimentalFoundationApi
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel,
    popBackStack: () -> Boolean,
    authorUsername: String,
    navigateToPhotoViewer: (photoUrl: String) -> Unit,
) {

    LaunchedEffect(null) {
        viewModel.getAuthorProfileData(authorUsername)
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
        },
    )
}

@ExperimentalFoundationApi
@Composable
internal fun AuthorDetailsScreenContent(
    authorData: ViewState<AuthorResponse>,
    authorUsername: String,
    authorPhotos: List<AuthorPhotosResponse>,
    authorPhotoState: ViewState<Unit>,
    popBackStack: () -> Boolean,
    getMorePhotos: () -> Unit,
    isPreview: Boolean = false,
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
                Text(authorUsername, style = AppTypoGraph.roboto_black().copy(fontSize = 16.sp))
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
                isPreview = isPreview
            )
            is ViewState.Error -> AuthorDetailsError { tryAgainGetAuthorData() }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun AuthorDetailsReady(
    authorData: AuthorResponse,
    authorPhotos: List<AuthorPhotosResponse>,
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
                style = AppTypoGraph.roboto_bold()
                    .copy(fontSize = 14.sp, color = MaterialTheme.colors.background)
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
fun UserProfilePreview() {

    MyApplicationTheme {
        Scaffold() {
            AuthorDetailsScreenContent(
                authorData = ViewState.Ready(
                    AuthorResponse(
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
                        following = 200
                    )
                ),
                getMorePhotos = {},
                popBackStack = { true },
                navigateToPhotoViewer = {},
                tryAgainGetAuthorData = {},
                isPreview = true,
                authorUsername = "JohnDoe",
                authorPhotoState = ViewState.Ready(Unit),
                authorPhotos = listOf()
            )
        }
    }
}
