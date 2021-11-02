package br.com.alaksion.myapplication.ui.home.authhandler

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomSheetVisibility
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme

@Composable
fun AuthenticationHandlerScreen(
    viewModel: AuthHandlerViewModel,
    goToLoginScreen: () -> Unit,
    goToHomeScreen: () -> Unit,
    authCode: String?
) {
    val bottomSheetState = LocalBottomSheetVisibility.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = bottomSheetState.value) {
        bottomSheetState.value = false
    }

    LaunchedEffect(key1 = true) {
        viewModel.authenticateUser(authCode)
    }

    AuthHandlerContent(
        screenState = viewModel.authenticationState.value,
        onClickTryAgain = { viewModel.authenticateUser(authCode) },
        goToLoginScreen = { goToLoginScreen() }
    )

    viewModel.handleNavigationSuccess.observeEvent(lifecycleOwner) {
        goToHomeScreen()
    }

}

@Composable
fun AuthHandlerContent(
    screenState: ViewState<Unit>,
    onClickTryAgain: () -> Unit,
    goToLoginScreen: () -> Unit
) {
    ImagefyTheme {
        Scaffold {
            when (screenState) {
                is ViewState.Loading, is ViewState.Ready, is ViewState.Idle ->
                    AuthHandlerContentLoading()
                is ViewState.Error -> AuthHandlerContentError(onClickTryAgain, goToLoginScreen)
            }
        }
    }
}

@Composable
fun AuthHandlerContentLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProgressIndicator(
            modifier = Modifier
                .padding()
                .padding(bottom = 20.dp)
        )
        Text(
            "Validating authentication, this might take a while.",
            style = MaterialTheme.typography.h5
                .copy(textAlign = TextAlign.Center),
        )
    }
}

@Composable
fun AuthHandlerContentError(
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
            Text("Go to login screen", style = MaterialTheme.typography.body2)
        }
    }
}