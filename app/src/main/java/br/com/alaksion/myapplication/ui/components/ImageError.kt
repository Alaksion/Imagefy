package br.com.alaksion.myapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph

@Composable
fun ImageError() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding()
                .padding(bottom = 10.dp)
        )
        Text(
            "Whoops, an error occurred and this image could not be loaded",
            style = AppTypoGraph.body_14_bold()
        )
    }
}