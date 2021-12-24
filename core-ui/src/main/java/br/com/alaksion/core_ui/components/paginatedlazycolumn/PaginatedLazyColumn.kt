package br.com.alaksion.core_ui.components.paginatedlazycolumn

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.components.loaders.MorePhotosLoader
import br.com.alaksion.core_ui.extensions.onBottomReached

@Composable
fun PaginatedLazyColumn(
    modifier: Modifier = Modifier,
    state: PaginatedLazyColumnState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    indicator: @Composable () -> Unit = { MorePhotosLoader() },
    onListEnd: () -> Unit = {},
    paginatorOffset: Int = 0,
    content: LazyListScope.() -> Unit,
) {
    require(paginatorOffset >= 0) { "Paginator offset must be greater or equal than 0" }

    Box() {
        LazyColumn(
            modifier = modifier,
            state = state.lazyListState,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            content = content
        )

        state.lazyListState.onBottomReached(offset = paginatorOffset) {
            onListEnd()
        }

        if (state.showIndicator) {
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)) {
                indicator()
            }
        }
    }

}