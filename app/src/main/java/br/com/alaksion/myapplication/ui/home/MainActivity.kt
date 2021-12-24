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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alaksion.core_ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.ui.components.HomeScreenNavigationDrawer
import br.com.alaksion.myapplication.ui.navigator.HomeNavigator
import br.com.alaksion.myapplication.ui.navigator.HomeScreen
import br.com.alaksion.myapplication.ui.navigator.bottomnav.HomeBottomNavigation
import br.com.alaksion.myapplication.ui.navigator.navigateToLogin
import br.com.alaksion.myapplication.ui.navigator.navigateToUserProfile
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
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
                val systemUiController = rememberSystemUiController()
                val scope = rememberCoroutineScope()
                val colors = MaterialTheme.colors

                LaunchedEffect(key1 = viewModel.isConfigDarkMode.collectAsState().value) {
                    systemUiController.setStatusBarColor(
                        color = colors.background,
                        darkIcons = viewModel.isConfigDarkMode.value.not()
                    )
                }

                ProvideWindowInsets() {
                    val navController = rememberNavController()
                    val scaffoldState = rememberScaffoldState()
                    val currentUserData = viewModel.currentUserData
                    val isBottomsheetVisible = remember { mutableStateOf(false) }
                    val currentRoute = navController.currentBackStackEntryAsState()

                    LaunchedEffect(key1 = currentRoute.value) {
                        isBottomsheetVisible.value =
                            shouldShowBottomNav(currentRoute.value?.destination?.route)
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
                        drawerElevation = 0.dp,
                        drawerContent = {
                            HomeScreenNavigationDrawer(
                                userData = currentUserData.collectAsState().value,
                                navigateToAuthorProfile = {
                                    scope.launch {
                                        navigateToUserProfile(navController)
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
                            if (isBottomsheetVisible.value)
                                HomeBottomNavigation(navController = navController)
                        },
                    ) { screenPadding ->
                        HomeNavigator(
                            navHostController = navController,
                            modifier = Modifier.padding(screenPadding),
                            toggleDrawer = { toggleDrawer() },
                            userData = currentUserData.collectAsState().value,
                            updateUserData = { viewModel.updateUserData(it) }
                        )
                    }
                }
            }
        }
    }

    private fun shouldShowBottomNav(currentRoute: String?): Boolean {
        return currentRoute == HomeScreen.PhotosList().route || currentRoute == HomeScreen.SearchPhotos().route
    }

}
