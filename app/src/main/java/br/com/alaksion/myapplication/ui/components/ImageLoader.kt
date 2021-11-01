package br.com.alaksion.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ImageLoader(
    modifier: Modifier = Modifier,
    backgroundColor: String,
    circularReveal: CircularReveal? = null,
    imageUrl: String,
    contentDescription: String? = null
) {
    val color = remember {
        Color(android.graphics.Color.parseColor(backgroundColor))
    }

    GlideImage(
        modifier = modifier,
        contentScale = ContentScale.Crop,
        imageModel = imageUrl,
        circularReveal = circularReveal,
        contentDescription = contentDescription,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
            )
        },
        failure = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ImageError()
            }
        }
    )
}