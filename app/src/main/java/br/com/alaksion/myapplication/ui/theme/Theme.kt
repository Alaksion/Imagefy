package br.com.alaksion.myapplication.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val ColorPallet = lightColors(
    background = OffWhite,
    onBackground = BlackRussian,
    surface = BlackRussian,
    onSurface = OffWhite
)

@Composable
fun ImagefyTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colors = ColorPallet,
        shapes = Shapes,
        content = content
    )
}