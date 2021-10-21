package br.com.alaksion.myapplication.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val ColorPallet = lightColors(
    onPrimary = OffWhite,
    primary = BlackRussian,
    background = OffWhite,
    onBackground = BlackRussian,
    surface = OffWhite,
    onSurface = BlackRussian
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