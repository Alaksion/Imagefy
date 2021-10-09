package br.com.alaksion.myapplication.ui.home.authordetails.components.authorheader

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph

@Composable
fun AuthorHeaderItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = AppTypoGraph.roboto_bold().copy(fontSize = 16.sp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            label,
            style = AppTypoGraph.roboto_regular().copy(fontSize = 12.sp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}