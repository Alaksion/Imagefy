package br.com.alaksion.myapplication.ui.home.authordetails.components.authorphotos

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.ui.components.ImageError
import br.com.alaksion.myapplication.ui.components.MorePhotosLoader
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.components.TryAgain
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@Composable
fun AuthorPhotosList(
    viewState: ViewState<Unit>,
    photos: List<AuthorPhotosResponse>,
    modifier: Modifier = Modifier,
    onClickTryAgain: () -> Unit,
    navigateToPhotoViewer: (photoUrl: String) -> Unit,
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
            Box() {
                LazyVerticalGrid(
                    state = listState,
                    cells = GridCells.Fixed(3),
                    modifier = Modifier.scale(1.01f)
                ) {
                    itemsIndexed(photos) { index, authorPhoto ->

                        if (index == photos.lastIndex - 3) {
                            loadMorePhotos()
                        }

                        GlideImage(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .border(1.dp, Color.White)
                                .clickable {
                                    navigateToPhotoViewer(authorPhoto.photoId)
                                },
                            contentScale = ContentScale.Crop,
                            imageModel = authorPhoto.photoUrl,
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ProgressIndicator()
                                }
                            },
                            failure = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ImageError()
                                }
                            }
                        )
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

