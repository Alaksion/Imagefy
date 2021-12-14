package br.com.alaksion.myapplication.ui.home.photolist

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.components.TryAgain
import br.com.alaksion.core_ui.components.loaders.MorePhotosLoader
import br.com.alaksion.core_ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.common.extensions.onBottomReached
import br.com.alaksion.myapplication.common.extensions.safeFlowCollect
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomSheetVisibility
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoCard
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoListTopBar
import kotlinx.coroutines.flow.collect

@ExperimentalAnimationApi
@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val bottomSheetState = LocalBottomSheetVisibility.current

    LaunchedEffect(viewModel, lifeCycleOwner) {
        safeFlowCollect(lifeCycleOwner) {
            viewModel.eventHandler.collect { event ->
                when (event) {
                    is PhotoListEvents.ShowMorePhotosError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.load_more_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        bottomSheetState.value = true
    }

    PhotoListScreenContent(
        screenState = viewModel.screenState.collectAsState().value,
        photos = viewModel.photos.toList(),
        loadMorePhotos = { viewModel.loadMorePhotos() },
        onClickTryAgain = { viewModel.getImages() },
        navigateToAuthorDetails = navigateToAuthorDetails,
        isMorePhotosLoading = viewModel.isMorePhotosLoading.collectAsState().value,
        ratePhoto = { photo, isLike ->
            viewModel.ratePhoto(photo, isLike)
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
    ratePhoto: (photo: PhotoResponse, isLike: Boolean) -> Unit
) {
    val listState = rememberLazyListState()

    Column(modifier.fillMaxSize()) {
        PhotoListTopBar(userProfileUrl = userProfileUrl, toggleDrawer = toggleDrawer)

        when (screenState) {
            is ViewState.Loading, is ViewState.Idle -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { ProgressIndicator(Modifier.align(Alignment.Center)) }
            }
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
                        items(photos) { item ->
                            PhotoCard(
                                photoContent = item,
                                navigateToAuthor = { authorId -> navigateToAuthorDetails(authorId) },
                                ratePhoto = ratePhoto
                            )
                            listState.onBottomReached(offset = 2) {
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