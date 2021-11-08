package br.com.alaksion.myapplication.ui.home.photolist

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomSheetVisibility
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.components.loaders.MorePhotosLoader
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoCard
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoListTopBar

@ExperimentalAnimationApi
@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
) {
    val bottomSheetState = LocalBottomSheetVisibility.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(key1 = bottomSheetState.value) {
        bottomSheetState.value = true
    }

    LaunchedEffect(key1 = true) {
        viewModel.getImages()
    }

    LaunchedEffect(key1 = true) {
        viewModel.showMorePhotosError.observeEvent(lifeCycleOwner) {
            Toast.makeText(
                context,
                context.getString(R.string.load_more_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    PhotoListScreenContent(
        screenState = viewModel.screenState.value,
        photos = viewModel.photos.toList(),
        loadMorePhotos = { viewModel.loadMorePhotos() },
        onClickTryAgain = { viewModel.getImages() },
        navigateToAuthorDetails = navigateToAuthorDetails,
        isMorePhotosLoading = viewModel.isMorePhotosLoading.value,
        ratePhoto = { photoId, isLike ->
            viewModel.ratePhoto(photoId, isLike)
        },
        toggleDrawer = toggleDrawer,
        userProfileUrl = userProfileUrl
    )
}

@ExperimentalAnimationApi
@Composable
internal fun PhotoListScreenContent(
    screenState: ViewState<Unit>,
    photos: List<PhotoResponse>,
    modifier: Modifier = Modifier,
    onClickTryAgain: () -> Unit,
    loadMorePhotos: () -> Unit,
    isMorePhotosLoading: Boolean,
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    ratePhoto: (photoId: String, isLike: Boolean) -> Unit
) {
    val listState = rememberLazyListState()

    Column(modifier.fillMaxSize()) {
        PhotoListTopBar(userProfileUrl = userProfileUrl, toggleDrawer = toggleDrawer)

        when (screenState) {
            is ViewState.Loading, is ViewState.Idle -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { ProgressIndicator(Modifier.align(Alignment.Center)) }
            is ViewState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TryAgain(
                        message = stringResource(id = R.string.photo_list_error),
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
            is ViewState.Ready -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(state = listState) {
                        itemsIndexed(photos) { index, item ->
                            PhotoCard(
                                photoContent = item,
                                navigateToAuthor = { authorId -> navigateToAuthorDetails(authorId) },
                                ratePhoto = ratePhoto
                            )
                            if (index == photos.lastIndex - 2) {
                                loadMorePhotos()
                            }
                        }
                    }
                    if (isMorePhotosLoading) {
                        MorePhotosLoader(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding()
                                .padding(bottom = 20.dp)
                        )
                    }
                }
            }
        }

    }

}
