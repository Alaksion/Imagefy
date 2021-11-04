package br.com.alaksion.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph.AppTypography

private val darkColors = darkColors(
    onPrimary = BlackRussian,
    primary = OffWhite,
    background = BlackRussian,
    onBackground = OffWhite,
    surface = BlackRussian,
    onSurface = OffWhite,
    secondary = LightGreen,
    onSecondary = Color.White,
)

private val lightColors = lightColors(
    onPrimary = OffWhite,
    primary = BlackRussian,
    background = OffWhite,
    onBackground = BlackRussian,
    surface = OffWhite,
    onSurface = BlackRussian,
    secondary = LightGreen,
    onSecondary = Color.White
)

@Composable
fun ImagefyTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colors = if (isDarkTheme) darkColors else lightColors,
        shapes = Shapes,
        content = content,
        typography = AppTypography()
    )
}