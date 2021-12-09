package br.com.alaksion.core_ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import br.com.alaksion.core_ui.R

private val robotoFontFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_black, FontWeight.Black),
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_thin, FontWeight.Thin),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

@Composable
fun appTypography() = Typography(
    defaultFontFamily = robotoFontFamily,
    h1 = MaterialTheme.typography.h1,
    h2 = MaterialTheme.typography.h2,
    h3 = MaterialTheme.typography.h3,
    h4 = MaterialTheme.typography.h4,
    h5 = MaterialTheme.typography.h5,
    h6 = MaterialTheme.typography.h6,
    subtitle1 = MaterialTheme.typography.subtitle1,
    subtitle2 = MaterialTheme.typography.subtitle2,
    body1 = MaterialTheme.typography.body1,
    body2 = MaterialTheme.typography.body2,
    button = MaterialTheme.typography.button,
    caption = MaterialTheme.typography.caption,
    overline = MaterialTheme.typography.overline
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