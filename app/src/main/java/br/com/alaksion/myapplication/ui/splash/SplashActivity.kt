package br.com.alaksion.myapplication.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.ui.authentication.login.LoginActivity
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.home.HomeActivity
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
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
        setContent {
            SplashScreenContent(
                screenState = viewModel.authenticationState.value,
                onClickTryAgain = { viewModel.verifyUserIsLogged() },
                goToLoginScreen = {
                    LoginActivity.start(this)
                    this.finish()
                },
            )
        }
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
                this.finish()
            }
        }
    }

}

@Composable
fun SplashScreenContent(
    screenState: ViewState<Unit>,
    onClickTryAgain: () -> Unit,
    goToLoginScreen: () -> Unit,
    isPreview: Boolean = false
) {
    ImagefyTheme {
        Scaffold() {
            when (screenState) {
                is ViewState.Loading, is ViewState.Idle, is ViewState.Ready ->
                    SplashContentLoading(isPreview)
                is ViewState.Error -> SplashContentError(onClickTryAgain, goToLoginScreen)
            }
        }
    }
}

@Composable
fun AppLogo(
    isPreview: Boolean = false
) {
    if (isPreview) {
        Icon(
            imageVector = Icons.Default.Android, contentDescription = null, modifier = Modifier
                .padding()
                .padding(bottom = 10.dp)
        )
    } else {
        val context = LocalContext.current
        val icon = context.packageManager.getApplicationIcon(context.applicationInfo)
        Image(
            painter = rememberDrawablePainter(drawable = icon),
            contentDescription = null,
            modifier = Modifier
                .padding()
                .padding(bottom = 10.dp)
        )
    }
}

@Composable
fun SplashContentError(
    onClickTryAgain: () -> Unit,
    goToLoginScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TryAgain(
            message = "An error occurred and your login could not be authenticated, please try again.",
            icon = {
                Icon(
                    imageVector = Icons.Default.Report,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(40.dp)
                )
            },
            onClick = { onClickTryAgain() },
            modifier = Modifier.padding(horizontal = 40.dp)
        )
        OutlinedButton(
            onClick = { goToLoginScreen() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 25.dp)
                .height(48.dp)
        ) {
            Text(
                "Go to login screen", style = AppTypoGraph.roboto_bold()
                    .copy(
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 14.sp
                    )
            )
        }
    }
}

@Composable
fun SplashContentLoading(
    isPreview: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppLogo(isPreview)
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

@Composable
@Preview
fun SplashScreenPreview() {
    ImagefyTheme {
        Scaffold() {
            SplashScreenContent(
                screenState = ViewState.Error(),
                onClickTryAgain = { /*TODO*/ },
                goToLoginScreen = {},
                isPreview = true
            )
        }
    }
}