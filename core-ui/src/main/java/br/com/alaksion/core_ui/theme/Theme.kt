package br.com.alaksion.core_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDarkMode) darkColors else lightColors

    MaterialTheme(
        colors = colors,
        typography = appTypography(),
        content = content
    )

}