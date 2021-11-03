package br.com.alaksion.myapplication.ui.home.searchphotos

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraRoll
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomSheetVisibility
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.components.ImageLoader
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.components.loaders.MorePhotosLoader
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.home.searchphotos.components.SearchPhotosTopBar
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme

@ExperimentalFoundationApi
@Composable
fun SearchPhotosScreen(
    viewModel: SearchPhotosViewModel,
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
) {
    val bottomSheetState = LocalBottomSheetVisibility.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(bottomSheetState.value) {
        bottomSheetState.value = true
    }


    LaunchedEffect(true) {
        viewModel.showMorePhotosError.observeEvent(lifeCycleOwner) {
            Toast.makeText(context, "Could not load more images, try again later", Toast.LENGTH_SHORT)
                .show()
        }
    }

    SearchPhotosContent(
        toggleDrawer,
        userProfileUrl,
        searchPhotos = { viewModel.searchPhotos() },
        onClickTryAgain = { viewModel.searchPhotos() },
        query = viewModel.searchQuery.value,
        onChangeQuery = { value -> viewModel.onChangeSearchQuery(value) },
        screenState = viewModel.screenState.value,
        isMorePhotosLoading = viewModel.isMorePhotosLoading.value,
        photos = viewModel.photoList,
        loadMorePhotos = { viewModel.loadMorePhotos() }
    )
}

@ExperimentalFoundationApi
@Composable
fun SearchPhotosContent(
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
    isPreview: Boolean = false,
    photos: List<PhotoResponse>,
    searchPhotos: () -> Unit,
    query: String,
    onChangeQuery: (value: String) -> Unit,
    screenState: ViewState<Unit>,
    isMorePhotosLoading: Boolean,
    loadMorePhotos: () -> Unit,
    onClickTryAgain: () -> Unit
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchPhotosTopBar(
            toggleDrawer = toggleDrawer,
            userProfileUrl = userProfileUrl,
            isPreview = isPreview,
            searchQuery = query,
            onSearchQueryChange = { value -> onChangeQuery(value) },
            onSubmitQuery = searchPhotos
        )

        when (screenState) {
            is ViewState.Idle -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ImageSearch,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .padding()
                            .padding(bottom = 10.dp),
                        tint = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "Whoops nothing to see here yet, try to search for something cute like fluffy cats.",
                        style = MaterialTheme.typography.h6.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            is ViewState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProgressIndicator()
                }
            }
            is ViewState.Ready -> Box(Modifier.fillMaxSize()) {
                if (photos.isEmpty()) {
                    SearchPhotosEmpty(Modifier.align(Alignment.Center))
                } else {
                    SearchPhotosList(
                        photos = photos,
                        listState = listState,
                        onLoadMorePhotos = loadMorePhotos,
                        isPreview = isPreview,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (isMorePhotosLoading) MorePhotosLoader(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding()
                            .padding(20.dp)
                            .zIndex(1f)
                    )
                }

            }
            is ViewState.Error -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TryAgain(
                    message = "An error occurred and your login could not be authenticated, please try again.",
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
}


@Composable
fun SearchPhotosEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ImageSearch,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding()
                .padding(bottom = 10.dp),
            tint = MaterialTheme.colors.onBackground
        )
        Text(
            text = "Whoops, no results were found.",
            style = MaterialTheme.typography.h6.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun SearchPhotosList(
    photos: List<PhotoResponse>,
    listState: LazyListState,
    onLoadMorePhotos: () -> Unit,
    modifier: Modifier = Modifier,
    isPreview: Boolean = false,
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        state = listState,
        modifier = modifier.scale(1.01f)
    ) {
        itemsIndexed(photos) { index, item ->
            if (index == photos.lastIndex - 3) onLoadMorePhotos()

            if (isPreview) {
                Icon(
                    imageVector = Icons.Default.CameraRoll,
                    tint = MaterialTheme.colors.onBackground,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(20.dp)
                )
            } else {
                ImageLoader(backgroundColor = item.color,
                    imageUrl = item.photoUrl,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(1.dp, Color.White)
                        .clickable {
                        }
                )
            }
        }
    }
}


@ExperimentalFoundationApi
@Composable
@Preview(showBackground = true)
fun SearchPhotosScreenPreview() {
    ImagefyTheme {
        Scaffold() {
            SearchPhotosContent(
                toggleDrawer = {},
                userProfileUrl = "",
                isPreview = true,
                query = "",
                onChangeQuery = {},
                searchPhotos = {},
                screenState = ViewState.Ready(Unit),
                isMorePhotosLoading = true,
                photos = (1..40).toList().map {
                    PhotoResponse(
                        "", 0, "", "", "", "", "", false, ""
                    )
                },
                loadMorePhotos = {},
                onClickTryAgain = {}
            )
        }
    }
}