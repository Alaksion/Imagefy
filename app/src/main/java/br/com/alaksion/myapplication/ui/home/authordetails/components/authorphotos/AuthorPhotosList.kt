package br.com.alaksion.myapplication.ui.home.authordetails.components.authorphotos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraRoll
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.common.extensions.onBottomReached
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.ui.components.ImageLoader
import br.com.alaksion.core_ui.components.TryAgain
import br.com.alaksion.core_ui.components.loaders.MorePhotosLoader
import br.com.alaksion.core_ui.components.loaders.ProgressIndicator

@ExperimentalFoundationApi
@Composable
fun AuthorPhotosList(
    viewState: ViewState<Unit>,
    photos: List<AuthorPhotosResponse>,
    modifier: Modifier = Modifier,
    onClickTryAgain: () -> Unit,
    navigateToPhotoViewer: (photoUrl: String) -> Unit,
    isPreview: Boolean = false,
    loadMorePhotos: () -> Unit
) {
    val listState = rememberLazyListState()

    when (viewState) {
        is ViewState.Loading, is ViewState.Idle -> {
            if (photos.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProgressIndicator()
                }
            }
        }
        is ViewState.Error -> {
            if (photos.isEmpty()) {
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
                        onClick = { onClickTryAgain() },
                        modifier = Modifier.padding(horizontal = 40.dp)
                    )
                }
            }
        }
        is ViewState.Ready -> {
            Box(Modifier.fillMaxSize()) {
                if (photos.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Camera,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground,
                            modifier = Modifier
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        color = MaterialTheme.colors.onBackground
                                    ),
                                    CircleShape
                                )
                        )
                        Text(
                            "This user has no photos uploaded",
                            style = MaterialTheme.typography.h6.copy(
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.onBackground
                            )
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        state = listState,
                        cells = GridCells.Fixed(3),
                        modifier = Modifier.scale(1.01f)
                    ) {
                        items(photos) { authorPhoto ->

                            listState.onBottomReached(offset = 3) {
                                loadMorePhotos()
                            }

                            if (isPreview) {
                                Icon(imageVector = Icons.Default.CameraRoll,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .border(1.dp, color = MaterialTheme.colors.onBackground)
                                        .clickable {
                                            navigateToPhotoViewer(authorPhoto.photoId)
                                        })

                            } else {
                                ImageLoader(
                                    backgroundColor = authorPhoto.color,
                                    imageUrl = authorPhoto.photoUrl,
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .border(1.dp, color = MaterialTheme.colors.background)
                                        .clickable {
                                            navigateToPhotoViewer(authorPhoto.photoId)
                                        }
                                )
                            }
                        }
                    }
                    if (viewState is ViewState.Loading && photos.isNotEmpty()) {
                        MorePhotosLoader(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding()
                                .padding(bottom = 15.dp)
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
@Preview(showBackground = true)
fun AuthorPhotoListPreview() {
    ImagefyTheme {
        Scaffold {
            AuthorPhotosList(
                viewState = ViewState.Ready(Unit),
                photos = listOf(),
                onClickTryAgain = { },
                navigateToPhotoViewer = {},
                loadMorePhotos = {}
            )
        }
    }
}

