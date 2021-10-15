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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alaksion.myapplication.ui.home.components.navigationdrawer.HomeScreenNavigationDrawer
import br.com.alaksion.myapplication.ui.home.navigator.HomeBottomNavigation
import br.com.alaksion.myapplication.ui.home.navigator.HomeNavigator
import br.com.alaksion.myapplication.ui.home.navigator.HomeScreen
import br.com.alaksion.myapplication.ui.home.navigator.navigateToUserProfile
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import br.com.alaksion.myapplication.ui.theme.MyApplicationTheme
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
            MyApplicationTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                Scaffold(
                    bottomBar = {
                        if (shouldShowBottomBar(
                                navController.currentBackStackEntryAsState().value?.destination?.route
                            )
                        ) {
                            HomeBottomNavigation(navController = navController)
                        }
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
                            drawerState = drawerState,
                            userData = currentUserData
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

    private fun shouldShowBottomBar(currentRoute: String?): Boolean {
        val screensWithoutBottomBar = listOf(
            HomeScreen.UserProfile().route,
            HomeScreen.AuthorDetails().route
        )

        return screensWithoutBottomBar.contains(currentRoute).not()
    }
}
