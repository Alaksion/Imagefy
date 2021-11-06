package br.com.alaksion.myapplication.ui.home.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomSheetVisibility
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import com.skydoves.landscapist.rememberDrawablePainter

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    updateUserData: (CurrentUserData) -> Unit
) {
    val bottomSheetState = LocalBottomSheetVisibility.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = bottomSheetState.value) {
        bottomSheetState.value = false
    }

    LaunchedEffect(key1 = true) {
        viewModel.verifyUserIsLogged()

        viewModel.isUserLogged.observeEvent(lifeCycleOwner) { isUserLogged ->
            if (isUserLogged) navigateToHome()
            else navigateToLogin()
        }

        viewModel.currentUserData.observeEvent(lifeCycleOwner) {
            updateUserData(it)
        }
    }

    SplashScreenContent(
        screenState = viewModel.authenticationState.value,
        onClickTryAgain = { viewModel.verifyUserIsLogged() },
        goToLoginScreen = { navigateToLogin() },
    )

}

@Composable
fun SplashScreenContent(
    screenState: ViewState<Unit>,
    onClickTryAgain: () -> Unit,
    goToLoginScreen: () -> Unit,
    isPreview: Boolean = false
) {
    when (screenState) {
        is ViewState.Loading, is ViewState.Idle, is ViewState.Ready ->
            SplashContentLoading(isPreview)
        is ViewState.Error -> SplashContentError(onClickTryAgain, goToLoginScreen)
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
                "Go to login screen", style = MaterialTheme.typography.body2.copy(
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold
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
            style = MaterialTheme.typography.h5.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground
            ),
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
            style = MaterialTheme.typography.body2.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground
            )
        )
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    ImagefyTheme(true) {
        Scaffold() {
            SplashScreenContent(
                screenState = ViewState.Loading (),
                onClickTryAgain = { /*TODO*/ },
                goToLoginScreen = {},
                isPreview = true
            )
        }
    }
}