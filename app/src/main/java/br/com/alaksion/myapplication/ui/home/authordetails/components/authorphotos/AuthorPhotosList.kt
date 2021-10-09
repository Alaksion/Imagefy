package br.com.alaksion.myapplication.ui.home.authordetails.components.authorphotos

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.ui.components.*
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@Composable
fun AuthorPhotosList(
    photos: ViewState<List<AuthorPhotosResponse>>,
    modifier: Modifier = Modifier,
    onClickTryAgain: () -> Unit,
    navigateToPhotoViewer: (photoUrl: String) -> Unit,
    loadMorePhotos: () -> Unit
) {
    val authorPhotos = remember { mutableStateListOf<AuthorPhotosResponse>() }
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = photos) {
        if (photos is ViewState.Ready) {
            authorPhotos.addAll(photos.data)
        }
    }

    if (photos is ViewState.Loading && authorPhotos.size == 0) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ProgressIndicator()
        }
    }

    if (photos is ViewState.Error && authorPhotos.isEmpty()) {
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

    Box {
        LazyVerticalGrid(
            state = listState,
            cells = GridCells.Fixed(3),
            modifier = Modifier.scale(1.01f)
        ) {
            items(authorPhotos) { authorPhoto ->
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
        InfiniteListHandler(listState = listState) { loadMorePhotos() }

        if (photos is ViewState.Loading && authorPhotos.size > 0) {
            MorePhotosLoader(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding()
                    .padding(bottom = 15.dp)
            )
        }
    }
}

