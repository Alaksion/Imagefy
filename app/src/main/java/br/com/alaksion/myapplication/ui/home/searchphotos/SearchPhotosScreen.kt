package br.com.alaksion.myapplication.ui.home.searchphotos

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.components.ImageError
import br.com.alaksion.myapplication.ui.components.loaders.MorePhotosLoader
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.home.searchphotos.components.SearchPhotosTopbar
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@Composable
fun SearchPhotosScreen(
    viewModel: SearchPhotosViewModel,
    toggleDrawer: () -> Unit,
    userProfileUrl: String
) {
    SearchPhotosContent(
        toggleDrawer,
        userProfileUrl,
        searchPhotos = { viewModel.searchPhotos() },
        query = viewModel.searchQuery.value,
        onChangeQuery = { value -> viewModel.onChangeSearchQuery(value) },
        screenState = viewModel.screenState.value,
        isMorePhotosLoading = viewModel.isMorePhotosLoading.value,
        photos = viewModel.photoList,
        loadMorePhotos = {viewModel.loadMorePhotos()}
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
    loadMorePhotos: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchPhotosTopbar(
            toggleDrawer = toggleDrawer,
            userProfileUrl = userProfileUrl,
            isPreview = isPreview,
            searchQuery = query,
            onSearchQueryChange = { value -> onChangeQuery(value) },
            onSubmitQuery = searchPhotos
        )

        when (screenState) {
            is ViewState.Idle -> SearchPhotosIdle()
            is ViewState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProgressIndicator()
                }
            }
            is ViewState.Error -> {
            }
            is ViewState.Ready -> SearchPhotosReady(
                photos = photos,
                isMorePhotosLoading = isMorePhotosLoading,
                isPreview = isPreview,
                loadMorePhotos
            )
        }

    }

}

@Composable
fun SearchPhotosIdle() {
    Column(
        modifier = Modifier
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
            text = "Whoops nothing to see here yet, try to search for something cute like fluffy cats.",
            style = AppTypoGraph.roboto_bold().copy(
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun SearchPhotosReady(
    photos: List<PhotoResponse>,
    isMorePhotosLoading: Boolean,
    isPreview: Boolean = false,
    loadMorePhotos: () -> Unit
) {

    val listState = rememberLazyListState()

    Box(Modifier.fillMaxSize()) {
        LazyVerticalGrid(cells = GridCells.Fixed(3), state = listState) {
            itemsIndexed(photos) { index, item ->
                if (index == photos.lastIndex - 6) loadMorePhotos()

                if (isPreview) {
                    Box(Modifier.background(Color.Red))
                } else {
                    GlideImage(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .border(1.dp, Color.White)
                            .clickable {
                            },
                        contentScale = ContentScale.Crop,
                        imageModel = item.photoUrl,
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
        }
        if (isMorePhotosLoading) MorePhotosLoader(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding()
                .padding(20.dp)
        )
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
                isMorePhotosLoading = false,
                photos = (1..6).toList().map { _ ->
                    PhotoResponse(
                        "", 0, "", "", "", "", "", false
                    )
                },
                loadMorePhotos = {}
            )
        }
    }
}