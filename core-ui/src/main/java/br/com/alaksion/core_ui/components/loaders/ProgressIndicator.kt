package br.com.alaksion.core_ui.components.loaders

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.theme.ImagefyTheme


@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    width: Dp = 3.dp,
    tint: Color = MaterialTheme.colors.onBackground
) {
    CircularProgressIndicator(color = tint, strokeWidth = width, modifier = modifier)
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    ImagefyTheme {
        ProgressIndicator()
    }
}