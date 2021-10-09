package br.com.alaksion.myapplication.ui.home.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.alaksion.myapplication.ui.home.authordetails.AUTHOR_USERNAME_ARG
import br.com.alaksion.myapplication.ui.home.authordetails.AuthorDetailsScreen
import br.com.alaksion.myapplication.ui.home.photolist.PhotoListScreen
import br.com.alaksion.myapplication.ui.home.photolist.PhotoListViewModel
import br.com.alaksion.myapplication.ui.home.photoviewer.PHOTO_ID_ARG
import br.com.alaksion.myapplication.ui.home.photoviewer.PhotoViewerScreen

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun HomeNavigator(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = HomeScreens.PhotosList().route,
        modifier = modifier
    ) {
        composable(route = HomeScreens.PhotosList().route) {
            val viewModel = hiltViewModel<PhotoListViewModel>(
                navHostController.getViewModelStoreOwner(navHostController.graph.id)
            )
            PhotoListScreen(
                viewModel,
                navigateToAuthorDetails = { authorId ->
                    navigateToAuthorDetails(
                        navHostController,
                        authorId
                    )
                }
            )
        }

        composable(
            route = "${HomeScreens.AuthorDetails().route}/{$AUTHOR_USERNAME_ARG}",
            arguments = listOf(
                navArgument(AUTHOR_USERNAME_ARG) {
                    type = NavType.StringType
                }
            )
        ) {
            it.arguments?.getString(AUTHOR_USERNAME_ARG)?.let { authorUsername ->
                AuthorDetailsScreen(
                    viewModel = hiltViewModel(),
                    authorUsername = authorUsername,
                    popBackStack = { navHostController.popBackStack() },
                    navigateToPhotoViewer = { photoUrl ->
                        navigateToPhotoViewer(navHostController, photoUrl)
                    }
                )
            }
        }

        composable(
            route = "${HomeScreens.PhotoViewer().route}/{$PHOTO_ID_ARG}",
            arguments = listOf(
                navArgument(PHOTO_ID_ARG) { type = NavType.StringType }
            )
        ) {
            it.arguments?.getString(PHOTO_ID_ARG)
                ?.let { photoId ->
                    PhotoViewerScreen(
                        viewModel = hiltViewModel(),
                        photoId = photoId,
                        popBackStack = { navHostController.popBackStack() }
                    )
                }
        }
    }

}

fun navigateToAuthorDetails(navHostController: NavHostController, authorUsername: String) {
    navHostController.navigate("${HomeScreens.AuthorDetails().route}/$authorUsername")
}

fun navigateToPhotoViewer(navHostController: NavHostController, photoUrl: String) {
    navHostController.navigate("${HomeScreens.PhotoViewer().route}/$photoUrl")
}