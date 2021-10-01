package br.com.alaksion.myapplication.ui.home.photolist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.components.InfiniteListHandler
import br.com.alaksion.myapplication.ui.components.MorePhotosLoader
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoCard
import br.com.alaksion.myapplication.ui.components.ProgressIndicator

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

    ConstraintLayout(modifier.fillMaxSize()) {
        val (list, screenLoader, morePhotosLoader) = createRefs()

        if (isScreenLoading) ProgressIndicator(Modifier.constrainAs(screenLoader) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        })
        else {
            LazyColumn(
                state = listState,
                modifier = Modifier.constrainAs(list) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            ) {
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
                    .constrainAs(morePhotosLoader) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(bottom = 20.dp)
            )
            InfiniteListHandler(listState = listState) { loadMorePhotos() }
        }
    }

}
