package br.com.alaksion.myapplication.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import br.com.alaksion.myapplication.ui.home.components.navigationdrawer.HomeScreenNavigationDrawer
import br.com.alaksion.myapplication.ui.home.navigator.HomeBottomNavigation
import br.com.alaksion.myapplication.ui.home.navigator.HomeNavigator
import br.com.alaksion.myapplication.ui.home.navigator.navigateToUserProfile
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@AndroidEntryPoint
@ExperimentalAnimationApi
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private val currentUserData: CurrentUserData by lazy { viewModel.getUserData() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImagefyTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val shouldShowBottomBar = remember { mutableStateOf(true) }

                fun toggleDrawer() {
                    scope.launch {
                        if (drawerState.isOpen) drawerState.close()
                        else drawerState.open()
                    }
                }

                Scaffold(
                    bottomBar = {
                        if (shouldShowBottomBar.value)
                            HomeBottomNavigation(navController = navController)
                    }
                ) { screenPadding ->
                    HomeScreenNavigationDrawer(
                        drawerState = drawerState,
                        userData = currentUserData,
                        navigateToAuthorProfile = {
                            scope.launch {
                                navigateToUserProfile(navController)
                                drawerState.close()
                            }
                        }
                    ) {
                        HomeNavigator(
                            navHostController = navController,
                            modifier = Modifier.padding(screenPadding),
                            toggleDrawer = { toggleDrawer() },
                            userData = currentUserData,
                            shouldShowBottomBar = shouldShowBottomBar
                        )
                    }
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }
}
