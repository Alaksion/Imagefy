package br.com.alaksion.myapplication.ui.home.photolist.components

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.components.ImageError
import br.com.alaksion.myapplication.ui.home.photolist.components.photoinfobottomsheet.PhotoInfoBottomSheet
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.DimGray
import br.com.alaksion.myapplication.ui.theme.ErrorLightRed
import br.com.alaksion.myapplication.ui.theme.MyApplicationTheme
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun PhotoCard(
    photoContent: PhotoResponse,
    modifier: Modifier = Modifier,
    navigateToAuthor: (authorId: String) -> Unit,
    ratePhoto: (photoId: String, isLike: Boolean) -> Unit
) {
    val isLiked = remember { mutableStateOf(photoContent.likedByUser) }
    val likeAnimationVisible = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val corroutineScope = rememberCoroutineScope()

    Box() {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .zIndex(0f)
        ) {
            PhotoCardHeader(
                imageUrl = photoContent.photoUrl,
                userName = photoContent.authorUserName,
                name = photoContent.authorName,
                profileImageUrl = photoContent.authorProfileThumbUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        navigateToAuthor(photoContent.authorUserName)
                    }
                    .padding(horizontal = 10.dp)
            )
            PhotoCardImage(
                photoContent.photoUrl,
                photoContent.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                corroutineScope.launch {
//                                    ratePhoto(photoContent.id, isLiked.value)
                                    if (!isLiked.value) {
                                        likeAnimationVisible.value = true
                                        delay(400)
                                        likeAnimationVisible.value = false
                                    }
                                    isLiked.value = isLiked.value.not()
                                }
                            }
                        )
                    }
            )
            PhotoCardInfo(
                likes = photoContent.likes,
                isLiked = isLiked.value,
                authorName = photoContent.authorName,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 15.dp)
            )
        }
        AnimatedVisibility(
            visible = likeAnimationVisible.value,
            enter = slideInVertically(
                initialOffsetY = {
                    with(density) { 420.dp.roundToPx() }
                },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = fadeOut(
                targetAlpha = 0f,
                animationSpec = tween(durationMillis = 600),
            ),
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colors.background.copy(alpha = 0.95f),
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center)
            )
        }
    }

}

@Composable
internal fun PhotoCardHeader(
    userName: String,
    name: String,
    profileImageUrl: String,
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    val fragManager = (LocalContext.current as AppCompatActivity).supportFragmentManager

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GlideImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(32.dp),
                imageModel = profileImageUrl,
                contentScale = ContentScale.Crop,
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
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier.background(DimGray)
                    )
                }
            )
            Column(
                modifier = Modifier
                    .padding()
                    .padding(start = 10.dp)
            ) {
                Text(userName, style = AppTypoGraph.roboto_bold().copy(fontSize = 14.sp))
                Text(name, style = AppTypoGraph.roboto_regular().copy(fontSize = 12.sp))
            }
        }

        IconButton(
            onClick = { PhotoInfoBottomSheet.show(fragManager, imageUrl) },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
internal fun PhotoCardImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    GlideImage(
        imageModel = imageUrl,
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer()
            )
        },
        failure = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { ImageError() }
        }
    )
}

@Composable
internal fun PhotoCardInfo(
    isLiked: Boolean,
    modifier: Modifier = Modifier,
    likes: Int,
    authorName: String
) {
    val iconTint = if (isLiked) ErrorLightRed else MaterialTheme.colors.onBackground
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding()
                .padding(top = 5.dp)
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .size(30.dp)
                    .padding()
                    .padding(end = 5.dp)
            )
            Text(
                "$likes likes",
                style = AppTypoGraph.roboto_bold().copy(fontSize = 14.sp),
            )
        }
        Text(
            text = buildAnnotatedString {
                withStyle(AppTypoGraph.span_roboto_regular().copy(fontSize = 12.sp)) {
                    append("Photo by ")
                }
                withStyle(AppTypoGraph.span_roboto_bold().copy(fontSize = 12.sp)) {
                    append(authorName)
                }
                withStyle(AppTypoGraph.span_roboto_regular().copy(fontSize = 12.sp)) {
                    append(" on Unsplash.")
                }
            }
        )
    }

}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
private fun PhotoCardPreview() {
    MyApplicationTheme {
        PhotoCard(
            photoContent = PhotoResponse(
                id = "",
                createdAt = "",
                likes = 10,
                description = "",
                authorName = "Daniel Hartman",
                authorUserName = "Dan",
                photoUrl = "",
                authorProfileThumbUrl = "",
                likedByUser = true
            ),
            navigateToAuthor = {},
            ratePhoto = { _, _ -> }
        )
    }
}