package br.com.alaksion.myapplication.ui.home.authordetails.components.header

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun AuthorHeaderItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onBackground),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            label,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}