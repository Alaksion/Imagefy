package br.com.alaksion.myapplication.ui.home.authordetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.data.model.author.components.AuthorHeaderItem
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.ui.home.photolist.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer

const val AUTHOR_USERNAME_ARG = "author_username"

@Composable
fun AuthorDetailsScreen(
    viewModel: AuthorDetailsViewModel,
    popBackStack: () -> Boolean,
    authorUsername: String
) {

    LaunchedEffect(null) {
        viewModel.getAuthorProfileData(authorUsername)
    }

    AuthorDetailsScreen(
        authorData = viewModel.authorData.value,
        authorUsername = authorUsername,
        popBackStack = popBackStack
    )

}

@Composable
internal fun AuthorDetailsScreen(
    authorData: ViewState<AuthorResponse>,
    authorUsername: String,
    popBackStack: () -> Boolean
) {
    Column() {
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(authorUsername, style = AppTypoGraph.body_16_black())
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            when (authorData) {
                is ViewState.Loading -> AuthorDetailsLoading()
                is ViewState.Ready -> AuthorDetailsReady(authorData = authorData.data)
                is ViewState.Error -> AuthorDetailsError {}
                is ViewState.Idle -> Unit
            }
        }
    }
}

@Composable
fun AuthorDetailsReady(authorData: AuthorResponse) {
    Column() {
        AuthorDetailsHeader(
            profileImageUrl = authorData.profileImage,
            photoCount = authorData.totalPhotos,
            followersCount = authorData.followers,
            followingCount = authorData.following
        )
    }

}

@Composable
fun AuthorDetailsHeader(
    profileImageUrl: String,
    photoCount: Int,
    followersCount: Int,
    followingCount: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            imageModel = profileImageUrl,
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .weight(2.0f),
            loading = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .shimmer()
                )
            },
            failure = {
                Icon(
                    imageVector = Icons.Default.Person,
                    tint = MaterialTheme.colors.onBackground,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        )
        AuthorHeaderItem(
            value = photoCount.toString(),
            label = "Posts",
            modifier = Modifier
                .weight(1.0f)
                .padding()
                .padding(start = 20.dp)
        )
        AuthorHeaderItem(
            value = followersCount.toString(),
            label = "Followers",
            modifier = Modifier
                .weight(1.0f)
                .padding()
                .padding(start = 10.dp)
        )
        AuthorHeaderItem(
            value = followingCount.toString(),
            label = "Following",
            modifier = Modifier
                .weight(1.0f)
                .padding()
                .padding(start = 10.dp)
        )
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
