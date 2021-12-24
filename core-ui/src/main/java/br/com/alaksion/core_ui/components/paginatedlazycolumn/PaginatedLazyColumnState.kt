package br.com.alaksion.core_ui.components.paginatedlazycolumn

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class PaginatedLazyColumnState(
    val showIndicator: Boolean,
    val lazyListState: LazyListState
)

@Composable
fun rememberPaginatedLazyColumnState(
    lazyListState: LazyListState,
    showIndicator: Boolean
): PaginatedLazyColumnState {
    return remember {
        PaginatedLazyColumnState(
            lazyListState = lazyListState,
            showIndicator = showIndicator
        )
    }

}