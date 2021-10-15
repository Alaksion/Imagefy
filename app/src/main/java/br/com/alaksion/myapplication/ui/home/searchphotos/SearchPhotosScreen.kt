package br.com.alaksion.myapplication.ui.home.searchphotos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.alaksion.myapplication.ui.home.searchphotos.components.SearchPhotosTopbar
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme

@Composable
fun SearchPhotosScreen(
    viewModel: SearchPhotosViewModel,
    toggleDrawer: () -> Unit,
    userProfileUrl: String
) {
    SearchPhotosContent(toggleDrawer, userProfileUrl)
}

@Composable
fun SearchPhotosContent(
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
    isPreview: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchPhotosTopbar(
            toggleDrawer = toggleDrawer,
            userProfileUrl = userProfileUrl,
            isPreview = isPreview
        )
    }

}

@Composable
@Preview(showBackground = true)
fun SearchPhotosScreenPreview() {
    ImagefyTheme {
        Scaffold() {
            SearchPhotosContent(
                toggleDrawer = {},
                userProfileUrl = "",
                isPreview = true
            )
        }
    }
}