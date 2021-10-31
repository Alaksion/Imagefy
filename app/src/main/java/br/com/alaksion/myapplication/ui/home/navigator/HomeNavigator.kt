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
import br.com.alaksion.myapplication.ui.home.searchphotos.SearchPhotosScreen
import br.com.alaksion.myapplication.ui.home.searchphotos.SearchPhotosViewModel
import br.com.alaksion.myapplication.ui.home.userprofile.UserProfileScreen
import br.com.alaksion.myapplication.ui.home.userprofile.UserProfileViewModel
import br.com.alaksion.myapplication.ui.model.CurrentUserData

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun HomeNavigator(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    toggleDrawer: () -> Unit,
    userData: CurrentUserData,
) {
    NavHost(
        navController = navHostController,
        startDestination = HomeScreen.PhotosList().route,
        modifier = modifier
    ) {
        composable(route = HomeScreen.PhotosList().route) {
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
                },
                toggleDrawer = toggleDrawer,
                userProfileUrl = userData.profileImageUrl,

                )
        }

        composable(
            route = "${HomeScreen.AuthorDetails().route}/{$AUTHOR_USERNAME_ARG}",
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
                    navigateToPhotoViewer = { photoId ->
                        navigateToPhotoViewer(navHostController, photoId)
                    },
                )
            }
        }

        composable(
            route = "${HomeScreen.PhotoViewer().route}/{$PHOTO_ID_ARG}",
            arguments = listOf(
                navArgument(PHOTO_ID_ARG) { type = NavType.StringType }
            )
        ) {
            it.arguments?.getString(PHOTO_ID_ARG)
                ?.let { photoId ->
                    PhotoViewerScreen(
                        viewModel = hiltViewModel(),
                        photoId = photoId,
                        popBackStack = { navHostController.popBackStack() },

                        )
                }
        }

        composable(
            route = HomeScreen.UserProfile().route
        ) {
            val viewModel = hiltViewModel<UserProfileViewModel>(
                navHostController.getViewModelStoreOwner(navHostController.graph.id)
            )
            UserProfileScreen(
                viewModel = viewModel,
                popBackStack = { navHostController.popBackStack() },
                authorUsername = userData.userName,
                navigateToPhotoViewer = { photoId ->
                    navigateToPhotoViewer(navHostController, photoId)
                },
            )
        }

        composable(
            route = HomeScreen.SearchPhotos().route
        ) {
            val viewModel = hiltViewModel<SearchPhotosViewModel>(
                navHostController.getViewModelStoreOwner(navHostController.graph.id)
            )
            SearchPhotosScreen(
                viewModel = viewModel,
                toggleDrawer = toggleDrawer,
                userProfileUrl = userData.profileImageUrl,
            )
        }
    }

}

fun navigateToAuthorDetails(navHostController: NavHostController, authorUsername: String) {
    navHostController.navigate("${HomeScreen.AuthorDetails().route}/$authorUsername")
}

fun navigateToPhotoViewer(navHostController: NavHostController, photoUrl: String) {
    navHostController.navigate("${HomeScreen.PhotoViewer().route}/$photoUrl")
}

fun navigateToUserProfile(navHostController: NavHostController) {
    navHostController.navigate(HomeScreen.UserProfile().route)
}