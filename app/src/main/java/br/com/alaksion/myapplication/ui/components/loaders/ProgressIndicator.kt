package br.com.alaksion.myapplication.ui.components.loaders

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    width: Dp = 3.dp,
    tint: Color = MaterialTheme.colors.onBackground
) {
    CircularProgressIndicator(color = tint, strokeWidth = width, modifier = modifier)
}