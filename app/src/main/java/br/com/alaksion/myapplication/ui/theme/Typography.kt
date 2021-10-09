package br.com.alaksion.myapplication.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import br.com.alaksion.myapplication.R

object AppTypoGraph {

    private val robotoFontFamily = FontFamily(
        Font(R.font.roboto_regular, FontWeight.Normal),
        Font(R.font.roboto_black, FontWeight.Black),
        Font(R.font.roboto_light, FontWeight.Light),
        Font(R.font.roboto_thin, FontWeight.Thin),
        Font(R.font.roboto_bold, FontWeight.Bold)
    )

    @Composable
    fun roboto_black() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Black
    )

    @Composable
    fun roboto_bold() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Bold
    )

    @Composable
    fun roboto_regular() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal
    )

}