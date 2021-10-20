package br.com.alaksion.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * CustomLazyGrid is an alternative implementation to Lazy Vertical Grid with fixed cells, the advantages of using this
 * composable instead of the built-in alternative is to avoid @ExperimentalApis and solve the index out of bounds exception
 * that happens when the LazyGrid source list is cleared.
 *
 * @param listState lazyListState associated with the custom lazy grid
 *
 * @param items list of items that will populate the grid
 *
 * @param rowSize Amount of items per row
 *
 * @param rowHorizontalMargin Horizontal space between items inside a roll, default is 0.dp
 *
 * @param rowVerticalMargin Vertical margin between rows, default is 0.dp
 *
 * @param itemContent Content of the rows
 *
 * **/
@Composable
fun <T> CustomLazyGrid(
    listState: LazyListState,
    items: List<T>,
    modifier: Modifier = Modifier,
    rowSize: Int = 1,
    rowVerticalMargin: Dp = 0.dp,
    rowHorizontalMargin: Dp = 0.dp,
    itemContent: @Composable BoxScope.(index: Int, item: T) -> Unit
) {
    val rows = items.chunked(rowSize)
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(rowVerticalMargin),
        state = listState
    ) {
        itemsIndexed(rows) { index, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(rowHorizontalMargin)
            ) {
                for ((rowIndex, rowItem) in row.withIndex()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(1f / (rowSize - rowIndex))
                    ) {
                        itemContent(index, rowItem)
                    }
                }
            }

        }
    }
}