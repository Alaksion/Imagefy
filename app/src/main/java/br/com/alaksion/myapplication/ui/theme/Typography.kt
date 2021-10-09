package br.com.alaksion.myapplication.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    fun body_16_black() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontSize = 16.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Black
    )

    @Composable
    fun body_16_bold() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontSize = 16.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Bold
    )

    @Composable
    fun body_14_bold() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontSize = 14.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Bold
    )

    @Composable
    fun body_16() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontSize = 16.sp,
        fontFamily = robotoFontFamily,
    )

    @Composable
    fun body_14() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontSize = 16.sp,
        fontFamily = robotoFontFamily,
    )

    @Composable
    fun body_12_bold() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontSize = 12.sp,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Bold
    )

    @Composable
    fun body_12() = TextStyle(
        color = MaterialTheme.colors.onBackground,
        fontSize = 12.sp,
        fontFamily = robotoFontFamily,
    )

}