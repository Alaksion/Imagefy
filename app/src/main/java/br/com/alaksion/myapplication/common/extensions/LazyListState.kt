package br.com.alaksion.myapplication.common.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.collect

@Composable
fun LazyListState.onBottomReached(offset: Int = 0, onListEnd: () -> Unit) {
    require(offset >= 0) { "lazy list offset cannot be negative" }

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                this.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            // returning false because the list should not load more content if it's empty

            lastVisibleItem.index >= layoutInfo.totalItemsCount - offset - 1
        }
    }

    LaunchedEffect(key1 = shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { shouldLoad -> if (shouldLoad) onListEnd() }
    }


}