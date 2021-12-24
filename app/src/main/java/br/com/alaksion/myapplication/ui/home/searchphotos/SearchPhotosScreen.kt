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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import br.com.alaksion.core_ui.components.TryAgain
import br.com.alaksion.core_ui.components.loaders.MorePhotosLoader
import br.com.alaksion.core_ui.components.loaders.ProgressIndicator
import br.com.alaksion.core_ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.domain.model.Photo
import br.com.alaksion.myapplication.ui.components.ImageLoader
import br.com.alaksion.myapplication.ui.home.searchphotos.components.SearchPhotosTopBar
import br.com.alaksion.myapplication.ui.model.ViewState
import kotlinx.coroutines.flow.collect

@ExperimentalFoundationApi
@Composable
fun SearchPhotosScreen(
    viewModel: SearchPhotosViewModel,
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(lifeCycleOwner, viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchPhotosEvents.MorePhotosError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.load_more_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    SearchPhotosContent(
        toggleDrawer,
        userProfileUrl,
        searchPhotos = { viewModel.searchPhotos() },
        onClickTryAgain = { viewModel.searchPhotos() },
        query = viewModel.searchQuery.collectAsState().value,
        onChangeQuery = { value -> viewModel.onChangeSearchQuery(value) },
        screenState = viewModel.screenState.collectAsState().value,
        isMorePhotosLoading = viewModel.isMorePhotosLoading.collectAsState().value,
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
    photos: List<Photo>,
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
                        text = stringResource(id = R.string.search_photos_idle),
                        style = MaterialTheme.typography.h6.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onBackground
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
                    message = stringResource(id = R.string.search_photos_error),
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
            text = stringResource(id = R.string.search_photos_empty),
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
    photos: List<Photo>,
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
                        .border(1.dp, MaterialTheme.colors.background)
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
        Scaffold {
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
                    Photo(
                        "", 0, "", "", "", "", "", false, ""
                    )
                },
                loadMorePhotos = {},
                onClickTryAgain = {}
            )
        }
    }
}