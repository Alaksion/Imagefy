package br.com.alaksion.core_ui.components.loaders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.theme.ImagefyTheme

@Composable
fun MorePhotosLoader(modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.background)
            .padding(10.dp)
    ) {
        ProgressIndicator(
            tint = MaterialTheme.colors.onBackground, width = 2.dp,
            modifier = Modifier.size(20.dp)
        )
    }

}

@Preview
@Composable
fun MorePhotosLoaderPreview() {
    ImagefyTheme {
        MorePhotosLoader()
    }
}