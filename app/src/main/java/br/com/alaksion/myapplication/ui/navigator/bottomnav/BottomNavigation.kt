package br.com.alaksion.myapplication.ui.navigator.bottomnav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alaksion.core_ui.theme.ImagefyTheme

@Composable
fun HomeBottomNavigation(
    navController: NavHostController
) {
    val items = listOf(
        NavigationItem.PhotoList(),
        NavigationItem.SearchPhotos()
    )

    BottomNavigation(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry.value?.destination?.route

        items.forEach { navItem ->
            val isRouteSelected = currentRoute == navItem.route

            BottomNavigationItem(
                selected = isRouteSelected,
                onClick = { handleBottomNavClick(navController, navItem.route) },
                icon = {
                    Icon(
                        imageVector = navItem.icon(isRouteSelected),
                        contentDescription = navItem.route,
                    )
                },
                alwaysShowLabel = false,
            )
        }
    }
}

private fun handleBottomNavClick(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
@Preview(showBackground = true)
fun BottomBarPreview() {
    ImagefyTheme(true) {
        HomeBottomNavigation(navController = rememberNavController())
    }
}