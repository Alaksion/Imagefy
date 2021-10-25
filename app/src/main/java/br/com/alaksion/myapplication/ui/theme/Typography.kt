package br.com.alaksion.myapplication.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
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
    fun AppTypography() = Typography(
        defaultFontFamily = robotoFontFamily,
        h1 = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onBackground),
        h2 = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onBackground),
        h3 = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onBackground),
        h4 = MaterialTheme.typography.h4.copy(color = MaterialTheme.colors.onBackground),
        h5 = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.onBackground),
        h6 = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onBackground),
        subtitle1 = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onBackground),
        subtitle2 = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.onBackground),
        body1 = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onBackground),
        body2 = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.onBackground),
        button = MaterialTheme.typography.button.copy(color = MaterialTheme.colors.onBackground),
        caption = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onBackground),
        overline = MaterialTheme.typography.overline.copy(color = MaterialTheme.colors.onBackground)
    )

    @Composable
    fun span_roboto_bold() = SpanStyle(
        color = MaterialTheme.colors.onBackground,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Bold
    )

    @Composable
    fun span_roboto_regular() = SpanStyle(
        color = MaterialTheme.colors.onBackground,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal
    )

}