package br.com.alaksion.myapplication.ui.home.photoviewer.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhotoInfoItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    icon: @Composable () -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onClick?.let { it() } }, modifier = Modifier.height(48.dp)) {
            icon()
        }
        Text(
            text,
            style = MaterialTheme.typography.body2.copy(
                color = MaterialTheme.colors.background,
            ),
            modifier = Modifier
                .padding()
                .padding(start = 5.dp)
        )
    }
}