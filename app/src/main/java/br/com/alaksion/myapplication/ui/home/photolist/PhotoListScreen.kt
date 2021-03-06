package br.com.alaksion.myapplication.ui.home.photolist

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.components.tryagain.TryAgain
import br.com.alaksion.core_ui.components.loaders.ProgressIndicator
import br.com.alaksion.core_ui.components.paginatedlazycolumn.PaginatedLazyColumn
import br.com.alaksion.core_ui.components.paginatedlazycolumn.rememberPaginatedLazyColumnState
import br.com.alaksion.core_ui.extensions.onBottomReached
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.domain.model.Photo
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoCard
import br.com.alaksion.myapplication.ui.home.photolist.components.PhotoListTopBar
import br.com.alaksion.myapplication.ui.model.ViewState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collect

@ExperimentalAnimationApi
@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.events.collect { event ->
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

    PhotoListScreenContent(
        screenState = viewModel.screenState.collectAsState().value,
        loadMorePhotos = { viewModel.loadMorePhotos() },
        onClickTryAgain = { viewModel.getImages() },
        navigateToAuthorDetails = navigateToAuthorDetails,
        isMorePhotosLoading = viewModel.isMorePhotosLoading.collectAsState().value,
        ratePhoto = { photo, isLike ->
            viewModel.ratePhoto(photo, isLike)
        },
        toggleDrawer = toggleDrawer,
        userProfileUrl = userProfileUrl,
        isRefreshing = viewModel.isListRefreshing.collectAsState().value,
        onRefreshList = { viewModel.onRefreshList() }
    )
}

@ExperimentalAnimationApi
@Composable
internal fun PhotoListScreenContent(
    screenState: ViewState<SnapshotStateList<Photo>>,
    modifier: Modifier = Modifier,
    onClickTryAgain: () -> Unit,
    loadMorePhotos: () -> Unit,
    isMorePhotosLoading: Boolean,
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
    isRefreshing: Boolean,
    onRefreshList: () -> Unit,
    navigateToAuthorDetails: (authorId: String) -> Unit,
    ratePhoto: (photo: Photo, isLike: Boolean) -> Unit
) {
    val listState = rememberLazyListState()

    val paginatorState = rememberPaginatedLazyColumnState(
        lazyListState = listState,
        showIndicator = isMorePhotosLoading
    )

    val swipeState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

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
                    SwipeRefresh(state = swipeState, onRefresh = { onRefreshList() }) {
                        PaginatedLazyColumn(
                            state = paginatorState,
                            paginatorOffset = 2,
                            onListEnd = { loadMorePhotos() }
                        ) {
                            items(screenState.data) { item ->
                                PhotoCard(
                                    photoContent = item,
                                    navigateToAuthor = { authorId ->
                                        navigateToAuthorDetails(
                                            authorId
                                        )
                                    },
                                    ratePhoto = ratePhoto
                                )
                                listState.onBottomReached(offset = 2) {
                                    loadMorePhotos()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}