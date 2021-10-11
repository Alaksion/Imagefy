package br.com.alaksion.myapplication.ui.home.photoviewer

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.common.extensions.formatNumber
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.utils.downloadImage
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.ui.components.NumberScrollerAnimation
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.home.photoviewer.components.PhotoInfoItem
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.ErrorLightRed
import br.com.alaksion.myapplication.ui.theme.OffWhite
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch


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
        popBackStack = popBackStack,
        onRateImage = { isLike ->
            viewModel.ratePhoto(photoId, isLike)
        }
    )
}

@ExperimentalAnimationApi
@Composable
internal fun PhotoViewerScreen(
    photoData: ViewState<PhotoDetailResponse>,
    popBackStack: () -> Boolean,
    onRateImage: (isLike: Boolean) -> Unit
) {
    val showDropdown = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    fun toggleDropDown() {
        showDropdown.value = showDropdown.value.not()
    }

    fun downloadImage() {
        coroutineScope.launch {
            toggleDropDown()
            val url = (photoData as ViewState.Ready).data.downloadLink
            context.downloadImage(url)
        }
    }

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
                IconButton(onClick = { popBackStack() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground,
                    )
                }
                if (photoData is ViewState.Ready) {
                    Column {
                        IconButton(
                            onClick = { toggleDropDown() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground
                            )
                        }
                        DropdownMenu(
                            expanded = showDropdown.value,
                            onDismissRequest = { toggleDropDown() }
                        ) {
                            DropdownMenuItem(onClick = { downloadImage() }) {
                                Text("Save Image")
                            }
                        }
                    }
                }
            }
        }
        when (photoData) {
            is ViewState.Ready -> {
                PhotoViewerReady(
                    photoData = photoData.data,
                    context = context,
                    onRateImage = onRateImage
                )
            }
            is ViewState.Loading, is ViewState.Idle -> {
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
                        modifier = Modifier.padding(horizontal = 40.dp),
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
internal fun PhotoViewerReady(
    photoData: PhotoDetailResponse,
    context: Context,
    onRateImage: (isLike: Boolean) -> Unit
) {

    val showBottomBar = remember { mutableStateOf(true) }
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
        showBottomBar.value = showBottomBar.value.not()
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
//        onRateImage(isImageLiked.value)
        if (isImageLiked.value) imageLikes.value--
        else imageLikes.value++

        isImageLiked.value = isImageLiked.value.not()
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
                            style = AppTypoGraph.roboto_regular()
                                .copy(color = OffWhite, fontSize = 14.sp),
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
