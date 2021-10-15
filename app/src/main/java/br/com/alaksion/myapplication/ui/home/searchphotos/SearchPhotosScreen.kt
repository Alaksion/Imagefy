package br.com.alaksion.myapplication.ui.home.searchphotos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.alaksion.myapplication.ui.home.searchphotos.components.SearchPhotosTopbar

@Composable
fun SearchPhotosScreen(
    viewModel: SearchPhotosViewModel,
    drawerState: DrawerState,
    userProfileUrl: String
) {
    SearchPhotosContent(drawerState, userProfileUrl)
}

@Composable
fun SearchPhotosContent(
    drawerState: DrawerState,
    userProfileUrl: String
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchPhotosTopbar(drawerState = drawerState, userProfileUrl = userProfileUrl)
    }

}