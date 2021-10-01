package br.com.alaksion.myapplication.ui.home.authordetails.components.authorphotos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.ui.components.InfiniteListHandler
import br.com.alaksion.myapplication.ui.components.MorePhotosLoader
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun AuthorPhotosList(
    photos: ViewState<List<AuthorPhotosResponse>>,
    modifier: Modifier = Modifier,
    onClickTryAgain: () -> Unit,
    loadMorePhotos: () -> Unit
) {
    val authorPhotos = mutableListOf<AuthorPhotosResponse>()
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

    if (photos is ViewState.Error) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Deu erro", style = AppTypoGraph.body_16_black())
        }
    }

    Box {
        LazyColumn(state = listState) {
            items(authorPhotos) { photo ->
                GlideImage(imageModel = photo.photoUrl)
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