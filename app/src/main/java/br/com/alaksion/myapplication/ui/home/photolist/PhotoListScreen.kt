package br.com.alaksion.myapplication.ui.home.photolist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.components.InfiniteListHandler
import br.com.alaksion.myapplication.ui.components.MorePhotosLoader
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoCard

@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    navigateToPhotoDetails: (photoId: String) -> Unit
) {
    PhotoListScreen(
        screenState = viewModel.screenState.value,
        photos = viewModel.photos.toList(),
        loadMorePhotos = { viewModel.loadMorePhotos() },
        onClickTryAgain = { viewModel.getImages() },
        navigateToAuthorDetails = navigateToAuthorDetails,
        navigateToPhotoDetails = navigateToPhotoDetails
    )
}

@Composable
internal fun PhotoListScreen(
    screenState: ViewState<Unit>,
    photos: List<PhotoResponse>,
    modifier: Modifier = Modifier,
    onClickTryAgain: () -> Unit,
    loadMorePhotos: () -> Unit,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    navigateToPhotoDetails: (photoId: String) -> Unit
) {
    val listState = rememberLazyListState()
    Box(modifier.fillMaxSize()) {

        if (screenState is ViewState.Loading) {
            if (photos.isEmpty()) {
                ProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        if (screenState is ViewState.Error) {
            if (photos.isEmpty()) {
                TryAgain(
                    message = "An error occurred and images could not be found, please try again later",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground
                        )
                    },
                    onClick = { onClickTryAgain() })
            }
        }

        LazyColumn(state = listState) {
            items(photos) { item ->
                PhotoCard(
                    photoContent = item,
                    navigateToAuthor = { authorId -> navigateToAuthorDetails(authorId) },
                    navigateToPhotoDetails = { photoId -> navigateToPhotoDetails(photoId) }
                )
            }
        }
        if (screenState is ViewState.Loading && photos.isNotEmpty()) MorePhotosLoader(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding()
                .padding(bottom = 20.dp)
        )
        InfiniteListHandler(listState = listState) { loadMorePhotos() }

    }

}
