package br.com.alaksion.myapplication.ui.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import br.com.alaksion.myapplication.ui.home.navigator.HomeNavigator
import br.com.alaksion.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Scaffold {
                    val navController = rememberNavController()
                    HomeNavigator(
                        navHostController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    )
                }
            }
        }
    }
}
