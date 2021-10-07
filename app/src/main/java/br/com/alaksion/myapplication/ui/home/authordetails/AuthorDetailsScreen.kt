package br.com.alaksion.myapplication.ui.home.authordetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.home.authordetails.components.AuthorMediaLinks
import br.com.alaksion.myapplication.ui.home.authordetails.components.authorheader.AuthorDetailsHeader
import br.com.alaksion.myapplication.ui.home.authordetails.components.authorphotos.AuthorPhotosList
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph

const val AUTHOR_USERNAME_ARG = "author_username"

@ExperimentalFoundationApi
@Composable
fun AuthorDetailsScreen(
    viewModel: AuthorDetailsViewModel,
    popBackStack: () -> Boolean,
    authorUsername: String,
    navigateToPhotoViewer: (photoUrl: String) -> Unit
) {

    LaunchedEffect(null) {
        viewModel.getAuthorProfileData(authorUsername)
    }

    AuthorDetailsScreen(
        authorData = viewModel.authorData.value,
        authorUsername = authorUsername,
        popBackStack = popBackStack,
        authorPhotos = viewModel.authorPhotos.value,
        getMorePhotos = { viewModel.getMoreAuthorPhotos() },
        navigateToPhotoViewer = navigateToPhotoViewer
    )
}

@ExperimentalFoundationApi
@Composable
internal fun AuthorDetailsScreen(
    authorData: ViewState<AuthorResponse>,
    authorUsername: String,
    authorPhotos: ViewState<List<AuthorPhotosResponse>>,
    popBackStack: () -> Boolean,
    getMorePhotos: () -> Unit,
    navigateToPhotoViewer: (photoUrl: String) -> Unit
) {
    Column() {
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
                Text(authorUsername, style = AppTypoGraph.body_16_black())
            }
        }
        when (authorData) {
            is ViewState.Loading -> AuthorDetailsLoading()
            is ViewState.Ready -> AuthorDetailsReady(
                authorData = authorData.data,
                authorPhotos = authorPhotos,
                getMorePhotos = getMorePhotos,
                navigateToPhotoViewer = navigateToPhotoViewer
            )
            is ViewState.Error -> AuthorDetailsError {}
            is ViewState.Idle -> Unit
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun AuthorDetailsReady(
    authorData: AuthorResponse,
    authorPhotos: ViewState<List<AuthorPhotosResponse>>,
    getMorePhotos: () -> Unit,
    navigateToPhotoViewer: (photoUrl: String) -> Unit
) {
    Column() {
        AuthorDetailsHeader(
            profileImageUrl = authorData.profileImage,
            photoCount = authorData.totalPhotos,
            followersCount = authorData.followers,
            followingCount = authorData.following,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 10.dp)
        )
        Text(
            authorData.name,
            style = AppTypoGraph.body_14_bold(),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
        Text(
            authorData.bio,
            style = AppTypoGraph.body_14(),
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        AuthorMediaLinks(
            twitterUser = authorData.twitterUser,
            instagramUser = authorData.instagramUser,
            portfolioUrl = authorData.portfolioUrl,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        )
        AuthorPhotosList(
            photos = authorPhotos,
            onClickTryAgain = { },
            navigateToPhotoViewer = navigateToPhotoViewer
        ) { getMorePhotos() }
    }
}

@Composable
fun AuthorDetailsError(
    onTryAgain: () -> Unit
) {

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
