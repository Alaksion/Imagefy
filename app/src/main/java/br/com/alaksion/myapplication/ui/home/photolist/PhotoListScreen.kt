package br.com.alaksion.myapplication.ui.home.photolist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.components.InfiniteListHandler
import br.com.alaksion.myapplication.ui.components.MorePhotosLoader
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoCard

@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    navigateToPhotoDetails: (photoId: String) -> Unit
) {
    PhotoListScreen(
        photos = viewModel.photos.toList(),
        isScreenLoading = viewModel.isScreenLoading.value,
        isMorePhotosLoading = viewModel.isMorePhotosLoading.value,
        loadMorePhotos = { viewModel.loadMorePhotos() },
        navigateToAuthorDetails = navigateToAuthorDetails,
        navigateToPhotoDetails = navigateToPhotoDetails
    )
}

@Composable
internal fun PhotoListScreen(
    photos: List<PhotoResponse>,
    isScreenLoading: Boolean,
    isMorePhotosLoading: Boolean,
    modifier: Modifier = Modifier,
    loadMorePhotos: () -> Unit,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    navigateToPhotoDetails: (photoId: String) -> Unit
) {
    val listState = rememberLazyListState()

    Box(modifier.fillMaxSize()) {

        if (isScreenLoading) ProgressIndicator(Modifier.align(Alignment.Center))
        else {
            LazyColumn(state = listState) {
                items(photos) { item ->
                    PhotoCard(
                        photoContent = item,
                        navigateToAuthor = { authorId -> navigateToAuthorDetails(authorId) },
                        navigateToPhotoDetails = { photoId -> navigateToPhotoDetails(photoId) }
                    )
                }
            }
            if (isMorePhotosLoading) MorePhotosLoader(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding()
                    .padding(bottom = 20.dp)
            )
            InfiniteListHandler(listState = listState) { loadMorePhotos() }
        }
    }

}
