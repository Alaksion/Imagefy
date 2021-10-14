package br.com.alaksion.myapplication.ui.home.photolist

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.components.MorePhotosLoader
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoCard
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.rememberDrawablePainter
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    drawerState: DrawerState,
    userProfileUrl: String
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    PhotoListScreenContent(
        screenState = viewModel.screenState.value,
        photos = viewModel.photos.toList(),
        loadMorePhotos = { viewModel.loadMorePhotos() },
        onClickTryAgain = { viewModel.getImages() },
        navigateToAuthorDetails = navigateToAuthorDetails,
        isMorePhotosLoading = viewModel.isMorePhotosLoading.value,
        ratePhoto = { photoId, isLike ->
            viewModel.ratePhoto(photoId, isLike)
        },
        drawerState = drawerState,
        userProfileUrl = userProfileUrl
    )

    viewModel.showMorePhotosError.observeEvent(lifeCycleOwner) {
        Toast.makeText(context, "Could not load more images, try again later", Toast.LENGTH_SHORT)
            .show()
    }
}

@ExperimentalAnimationApi
@Composable
internal fun PhotoListScreenContent(
    screenState: ViewState<Unit>,
    photos: List<PhotoResponse>,
    modifier: Modifier = Modifier,
    onClickTryAgain: () -> Unit,
    loadMorePhotos: () -> Unit,
    isMorePhotosLoading: Boolean,
    drawerState: DrawerState,
    userProfileUrl: String,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    ratePhoto: (photoId: String, isLike: Boolean) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier.fillMaxSize()) {
        TopAppBar(
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.background,
            contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val appIcon = context.packageManager.getApplicationIcon(context.applicationInfo)
                GlideImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .clickable {
                            scope.launch {
                                if (drawerState.isOpen) drawerState.close()
                                else drawerState.open()
                            }
                        },
                    imageModel = userProfileUrl,
                    contentScale = ContentScale.Fit,
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
                            tint = MaterialTheme.colors.background,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.onBackground)
                                .padding(5.dp)
                        )
                    }
                )
                Image(
                    painter = rememberDrawablePainter(drawable = appIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding()
                        .padding(bottom = 10.dp)
                )
            }
        }

        when (screenState) {
            is ViewState.Loading, is ViewState.Idle -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { ProgressIndicator(Modifier.align(Alignment.Center)) }
            is ViewState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TryAgain(
                        message = "An error occurred and images could not be loaded, please try again later",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Report,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        onClick = { onClickTryAgain() },
                        modifier = Modifier.padding(horizontal = 40.dp)
                    )
                }
            }
            is ViewState.Ready -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(state = listState) {
                        itemsIndexed(photos) { index, item ->
                            PhotoCard(
                                photoContent = item,
                                navigateToAuthor = { authorId -> navigateToAuthorDetails(authorId) },
                                ratePhoto = ratePhoto
                            )
                            if (index == photos.lastIndex - 2) {
                                loadMorePhotos()
                            }
                        }
                    }
                    if (isMorePhotosLoading) {
                        MorePhotosLoader(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding()
                                .padding(bottom = 20.dp)
                        )
                    }
                }
            }
        }

    }

}
