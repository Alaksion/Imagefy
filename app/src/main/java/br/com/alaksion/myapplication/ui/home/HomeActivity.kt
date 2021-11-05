package br.com.alaksion.myapplication.ui.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomNavProvider
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomSheetVisibility
import br.com.alaksion.myapplication.ui.components.HomeScreenNavigationDrawer
import br.com.alaksion.myapplication.ui.navigator.HomeNavigator
import br.com.alaksion.myapplication.ui.navigator.bottomnav.HomeBottomNavigation
import br.com.alaksion.myapplication.ui.navigator.navigateToLogin
import br.com.alaksion.myapplication.ui.navigator.navigateToUserProfile
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@AndroidEntryPoint
@ExperimentalAnimationApi
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            LocalBottomNavProvider {
                ImagefyTheme() {
                    val navController = rememberNavController()
                    val scaffoldState = rememberScaffoldState()
                    val scope = rememberCoroutineScope()
                    val currentUserData = viewModel.currentUserData

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
                                }
                            )
                        },
                        bottomBar = {
                            if (LocalBottomSheetVisibility.current.value)
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
