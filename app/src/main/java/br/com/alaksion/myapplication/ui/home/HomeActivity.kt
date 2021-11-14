package br.com.alaksion.myapplication.ui.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomNavProvider
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomSheetVisibility
import br.com.alaksion.myapplication.ui.components.HomeScreenNavigationDrawer
import br.com.alaksion.myapplication.ui.navigator.HomeNavigator
import br.com.alaksion.myapplication.ui.navigator.bottomnav.HomeBottomNavigation
import br.com.alaksion.myapplication.ui.navigator.navigateToLogin
import br.com.alaksion.myapplication.ui.navigator.navigateToUserProfile
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        installSplashScreen()
        setContent {
            ImagefyTheme(isDarkTheme = viewModel.isConfigDarkMode.value) {
                val systemUiController = rememberSystemUiController()
                val scope = rememberCoroutineScope()
                val colors = MaterialTheme.colors

                LaunchedEffect(key1 = viewModel.isConfigDarkMode.value) {
                    systemUiController.setStatusBarColor(
                        color = colors.background,
                        darkIcons = viewModel.isConfigDarkMode.value.not()
                    )
                }

                ProvideWindowInsets() {
                    LocalBottomNavProvider {
                        val navController = rememberNavController()
                        val scaffoldState = rememberScaffoldState()
                        val currentUserData = viewModel.currentUserData
                        val isBottomsheetVisible = LocalBottomSheetVisibility.current

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
                                    userData = currentUserData.value,
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
                                userData = currentUserData.value,
                                updateUserData = { viewModel.updateUserData(it) }
                            )
                        }

                    }
                }
            }
        }
    }
}
