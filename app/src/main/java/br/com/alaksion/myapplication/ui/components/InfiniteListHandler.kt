package br.com.alaksion.myapplication.ui.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int = 2,
    onLoadMore: () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val itemCount = layoutInfo.totalItemsCount
            val lastVisibleIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            lastVisibleIndex > (itemCount - buffer)
        }
    }

    LaunchedEffect(key1 = loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect { onLoadMore() }
    }
}