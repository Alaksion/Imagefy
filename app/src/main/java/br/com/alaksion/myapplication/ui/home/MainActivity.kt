package br.com.alaksion.myapplication.ui.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alaksion.core_ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.ui.components.HomeScreenNavigationDrawer
import br.com.alaksion.myapplication.ui.home.authordetails.AuthorDetailsViewModel
import br.com.alaksion.myapplication.ui.home.photoviewer.PhotoViewerViewModel
import br.com.alaksion.myapplication.ui.navigator.HomeNavigator
import br.com.alaksion.myapplication.ui.navigator.bottomnav.HomeBottomNavigation
import br.com.alaksion.myapplication.ui.navigator.navigateToAuthorDetails
import br.com.alaksion.myapplication.ui.navigator.navigateToLogin
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        installSplashScreen()
        setContent {
            ImagefyTheme(isDarkMode = viewModel.isConfigDarkMode.collectAsState().value) {
                ProvideWindowInsets() {

                    val systemUiController = rememberSystemUiController()
                    val scope = rememberCoroutineScope()
                    val colors = MaterialTheme.colors
                    val navController = rememberNavController()
                    val scaffoldState = rememberScaffoldState()
                    val currentRoute = navController.currentBackStackEntryAsState()

                    LaunchedEffect(key1 = viewModel.isConfigDarkMode.collectAsState().value) {
                        systemUiController.setStatusBarColor(
                            color = colors.background,
                            darkIcons = viewModel.isConfigDarkMode.value.not()
                        )
                    }

                    LaunchedEffect(key1 = currentRoute.value) {
                        viewModel.showOrHideBottomNav(currentRoute.value?.destination?.route)
                        viewModel.enableOrDisableNavDrawer(currentRoute.value?.destination?.route)
                    }

                    fun toggleDrawer() {
                        scope.launch {
                            scaffoldState.run {
                                if (drawerState.isOpen) drawerState.close()
                                else drawerState.open()
                            }
                        }
                    }

                    Scaffold(
                        scaffoldState = scaffoldState,
                        drawerBackgroundColor = MaterialTheme.colors.background,
                        drawerGesturesEnabled = viewModel.isNavDrawerEnabled.collectAsState().value,
                        drawerElevation = 0.dp,
                        drawerContent = {
                            HomeScreenNavigationDrawer(
                                userData = viewModel.currentUserData.collectAsState().value,
                                navigateToAuthorProfile = {
                                    scope.launch {
                                        navigateToAuthorDetails(
                                            navController,
                                            viewModel.currentUserData.value.userName
                                        )
                                        scaffoldState.drawerState.close()
                                    }
                                },
                                onLogoutClick = {
                                    toggleDrawer()
                                    viewModel.clearAuthToken()
                                    navigateToLogin(navController)
                                },
                                toggleDarkMode = { viewModel.toggleDarkMode() }
                            )
                        },
                        bottomBar = {
                            if (viewModel.isBottomNavVisible.collectAsState().value)
                                HomeBottomNavigation(navController = navController)
                        },
                    ) { screenPadding ->
                        HomeNavigator(
                            navHostController = navController,
                            modifier = Modifier.padding(screenPadding),
                            toggleDrawer = { toggleDrawer() },
                            userData = viewModel.currentUserData.collectAsState().value,
                            updateUserData = { viewModel.updateUserData(it) }
                        )
                    }
                }
            }
        }
    }
}