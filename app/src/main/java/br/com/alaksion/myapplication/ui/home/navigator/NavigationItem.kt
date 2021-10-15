package br.com.alaksion.myapplication.ui.home.navigator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val icon: (isSelected: Boolean) -> ImageVector
) {

    class PhotoList :
        NavigationItem(route = HomeScreen.PhotosList().route, icon = { isSelected ->
            if (isSelected) Icons.Default.Home
            else Icons.Outlined.Home
        })

    class SearchPhotos : NavigationItem(HomeScreen.SearchPhotos().route, icon = { isSelected ->
        if (isSelected) Icons.Default.Search
        else Icons.Outlined.Search
    })

}