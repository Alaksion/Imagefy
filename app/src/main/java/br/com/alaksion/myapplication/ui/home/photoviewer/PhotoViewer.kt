package br.com.alaksion.myapplication.ui.home.photoviewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.extensions.formatNumber
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.home.photoviewer.components.PhotoInfoItem
import br.com.alaksion.myapplication.ui.theme.BlackRussian
import br.com.alaksion.myapplication.ui.theme.OffWhite
import com.skydoves.landscapist.glide.GlideImage


const val PHOTO_ID_ARG = "photo_url"

@ExperimentalAnimationApi
@Composable
fun PhotoViewerScreen(
    photoId: String,
    viewModel: PhotoViewerViewModel,
    popBackStack: () -> Boolean,
) {
    LaunchedEffect(null) {
        viewModel.getPhotoDetails(photoId)
    }

    PhotoViewerScreen(
        photoData = viewModel.photoData.value,
        popBackStack = popBackStack
    )
}

@ExperimentalAnimationApi
@Composable
internal fun PhotoViewerScreen(
    photoData: ViewState<PhotoDetailResponse>,
    popBackStack: () -> Boolean,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { popBackStack() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground,
                    )
                }
            }
        }
        when (photoData) {
            is ViewState.Ready -> {
                PhotoViewerReady(photoData = photoData.data)
            }
            is ViewState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ProgressIndicator()
                }
            }
            is ViewState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TryAgain(
                        message = "An error occurred and this image could not be loaded, please try again",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null
                            )
                        },
                        onClick = { /*TODO*/ })
                }
            }
        }
    }

}

@ExperimentalAnimationApi
@Composable
internal fun PhotoViewerReady(photoData: PhotoDetailResponse) {
    val showBottomBar = remember { mutableStateOf(true) }
    val density = LocalDensity.current

    fun toggleBottomBar() {
        showBottomBar.value = showBottomBar.value.not()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GlideImage(
            modifier = Modifier
                .fillMaxSize()
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    toggleBottomBar()
                },
            imageModel = photoData.imageUrl,
            contentScale = ContentScale.Crop,
            loading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProgressIndicator()
                }
            },
            failure = {}
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = showBottomBar.value,
            enter = slideInVertically(
                initialOffsetY = {
                    with(density) { 56.dp.roundToPx() }
                }
            ),
            exit = slideOutVertically(
                targetOffsetY = {
                    with(density) { 56.dp.roundToPx() }
                }
            )
        ) {
            BottomAppBar(
                backgroundColor = BlackRussian.copy(alpha = 0.3f),
                elevation = 0.dp
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PhotoInfoItem(
                        text = photoData.likes.formatNumber()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = OffWhite,
                        )
                    }

                    PhotoInfoItem(
                        text = photoData.downloads.formatNumber()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = null,
                            tint = OffWhite,
                        )
                    }

                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(48.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = null,
                            tint = OffWhite,
                        )
                    }
                }
            }
        }
    }
}
