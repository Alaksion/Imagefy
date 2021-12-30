package br.com.alaksion.myapplication.ui.home.photoviewer

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.components.loaders.ProgressIndicator
import br.com.alaksion.core_ui.components.tryagain.TryAgain
import br.com.alaksion.core_ui.theme.ErrorLightRed
import br.com.alaksion.core_ui.theme.OffWhite
import br.com.alaksion.myapplication.common.extensions.formatNumber
import br.com.alaksion.myapplication.common.extensions.invert
import br.com.alaksion.myapplication.domain.model.PhotoDetail
import br.com.alaksion.myapplication.ui.components.ImageLoader
import br.com.alaksion.myapplication.ui.components.NumberScrollerAnimation
import br.com.alaksion.myapplication.ui.home.photoviewer.components.PhotoInfoItem

const val PHOTO_ID_ARG = "photo_url"

@ExperimentalAnimationApi
@Composable
fun PhotoViewerScreen(
    photoId: String,
    viewModel: PhotoViewerViewModel,
    popBackStack: () -> Boolean,
) {

    PhotoViewerScreenContent(
        photoState = viewModel.photoData.collectAsState().value,
        popBackStack = popBackStack,
        onRateImage = { isLike ->
            viewModel.ratePhoto(photoId, isLike)
        },
        onClickTryAgain = { viewModel.getPhotoDetails() }
    )
}

@ExperimentalAnimationApi
@Composable
internal fun PhotoViewerScreenContent(
    photoState: PhotoViewerState,
    popBackStack: () -> Boolean,
    onRateImage: (isLike: Boolean) -> Unit,
    onClickTryAgain: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { popBackStack() },
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { testTag = PhotoViewerTags.BACK_BUTTON.name }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground,
                    )
                }
            }
        }
        when (photoState) {
            is PhotoViewerState.Ready -> {
                PhotoViewerReady(
                    photoData = photoState.photoData,
                    context = context,
                    onRateImage = onRateImage
                )
            }
            is PhotoViewerState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { testTag = PhotoViewerTags.LOADING.name },
                    contentAlignment = Alignment.Center
                ) {
                    ProgressIndicator()
                }
            }
            is PhotoViewerState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { testTag = PhotoViewerTags.ERROR.name },
                    contentAlignment = Alignment.Center
                ) {
                    TryAgain(
                        modifier = Modifier.padding(horizontal = 40.dp),
                        message = "An error occurred and this image could not be loaded, please try again",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null
                            )
                        },
                        onClick = { onClickTryAgain() })
                }
            }
        }
    }

}

@ExperimentalAnimationApi
@Composable
internal fun PhotoViewerReady(
    photoData: PhotoDetail,
    context: Context,
    onRateImage: (isLike: Boolean) -> Unit
) {

    val showPhotoViewerBottomBar = remember { mutableStateOf(true) }
    val density = LocalDensity.current
    val clipboardManager = LocalClipboardManager.current
    val isImageLiked = remember { mutableStateOf(photoData.likedByUser) }
    val imageLikes = remember { mutableStateOf(photoData.likes) }

    val likeIconColor =
        animateColorAsState(
            targetValue = if (isImageLiked.value) ErrorLightRed
            else OffWhite,
            animationSpec = tween(150)
        )

    fun toggleBottomBar() {
        showPhotoViewerBottomBar.value = showPhotoViewerBottomBar.value.not()
    }

    fun copyLinkToClipboard() {
        clipboardManager.setText(AnnotatedString(photoData.imageUrl))
        Toast.makeText(
            context,
            "Image link copied to clipboard",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun rateImage() {
        onRateImage(isImageLiked.value)
        if (isImageLiked.value) imageLikes.value--
        else imageLikes.value++

        isImageLiked.invert()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ImageLoader(backgroundColor = photoData.color,
            imageUrl = photoData.imageUrl,
            modifier = Modifier
                .fillMaxSize()
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    toggleBottomBar()
                }
                .semantics { testTag = PhotoViewerTags.IMAGE_LOADER.name }
        )
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .semantics { testTag = PhotoViewerTags.STATS_BAR.name },
            visible = showPhotoViewerBottomBar.value,
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
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(0.5f), Color.Black.copy(0.9f))
                        )
                    )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        modifier = Modifier.semantics { testTag = PhotoViewerTags.RATE_IMAGE.name },
                        onClick = { rateImage() },
                    ) {
                        Icon(
                            imageVector = if (isImageLiked.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = likeIconColor.value,
                        )
                    }
                    NumberScrollerAnimation(value = imageLikes.value) { currentValue ->
                        Text(
                            currentValue,
                            style = MaterialTheme.typography.body2
                                .copy(color = OffWhite),
                        )
                    }
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

                IconButton(
                    onClick = { copyLinkToClipboard() },
                    modifier = Modifier.size(48.dp)
                ) {
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
