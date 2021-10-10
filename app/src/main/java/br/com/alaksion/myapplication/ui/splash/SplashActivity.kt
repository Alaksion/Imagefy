package br.com.alaksion.myapplication.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.ui.authentication.login.LoginActivity
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.home.HomeActivity
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.MyApplicationTheme
import com.skydoves.landscapist.rememberDrawablePainter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent { SplashScreenContent() }
        setUpObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.verifyUserIsLogged()
    }

    private fun setUpObservers() {
        viewModel.isUserLogged.observeEvent(this) { isLogged ->
            if (isLogged) {
                HomeActivity.start(this)
            } else {
                LoginActivity.start(this)
            }
        }
    }


    @Composable
    fun SplashScreenContent() {
        MyApplicationTheme {
            Scaffold() {
                val icon = packageManager.getApplicationIcon(applicationInfo)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = rememberDrawablePainter(drawable = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding()
                            .padding(bottom = 10.dp)
                    )
                    Text(
                        "Imagefy",
                        style = AppTypoGraph.roboto_bold()
                            .copy(fontSize = 24.sp, textAlign = TextAlign.Center),
                        modifier = Modifier
                            .padding()
                            .padding(bottom = 10.dp)
                    )
                    ProgressIndicator(
                        modifier = Modifier
                            .padding()
                            .padding(bottom = 10.dp)
                    )
                    Text(
                        "Loading content...",
                        style = AppTypoGraph.roboto_bold()
                            .copy(fontSize = 14.sp, textAlign = TextAlign.Center)
                    )
                }
            }
        }
    }
}