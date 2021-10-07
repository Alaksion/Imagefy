package br.com.alaksion.myapplication.ui.home.authordetails.components.authorphotos

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
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
import br.com.alaksion.myapplication.ui.components.ImageError
import br.com.alaksion.myapplication.ui.components.InfiniteListHandler
import br.com.alaksion.myapplication.ui.components.MorePhotosLoader
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
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
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Deu erro", style = AppTypoGraph.body_16_black())
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

