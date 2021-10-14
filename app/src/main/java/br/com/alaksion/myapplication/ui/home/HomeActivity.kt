package br.com.alaksion.myapplication.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import br.com.alaksion.myapplication.ui.home.components.navigationdrawer.HomeScreenNavigationDrawer
import br.com.alaksion.myapplication.ui.home.navigator.HomeNavigator
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
                Scaffold {
                    val navController = rememberNavController()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    HomeScreenNavigationDrawer(
                        drawerState = drawerState,
                        userData = currentUserData,
                        navigateToAuthorProfile = {
                            scope.launch {
                                drawerState.close()
                                navigateToUserProfile(navController)
                            }
                        }
                    ) {
                        HomeNavigator(
                            navHostController = navController,
                            modifier = Modifier.fillMaxSize(),
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
}
