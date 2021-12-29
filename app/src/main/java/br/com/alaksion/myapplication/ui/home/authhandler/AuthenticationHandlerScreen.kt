package br.com.alaksion.myapplication.ui.home.authhandler

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.components.tryagain.TryAgain
import br.com.alaksion.core_ui.components.loaders.ProgressIndicator
import br.com.alaksion.core_ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.domain.model.StoredUser
import kotlinx.coroutines.flow.collect

@Composable
fun AuthenticationHandlerScreen(
    viewModel: AuthHandlerViewModel,
    goToLoginScreen: () -> Unit,
    goToHomeScreen: () -> Unit,
    authCode: String?,
    updateUserData: (StoredUser) -> Unit
) {

    LaunchedEffect(viewModel) {
        viewModel.authenticateUser(authCode)

        viewModel.events.collect { event ->
            when (event) {
                is AuthHandlerEvents.NavigateToSuccess -> goToHomeScreen()
                is AuthHandlerEvents.UpdateUserData -> updateUserData(event.data)
            }
        }
    }

    AuthHandlerContent(
        screenState = viewModel.authenticationState.collectAsState().value,
        onClickTryAgain = { viewModel.authenticateUser(authCode) },
        goToLoginScreen = { goToLoginScreen() }
    )

}

@Composable
fun AuthHandlerContent(
    screenState: AuthHandlerState,
    onClickTryAgain: () -> Unit,
    goToLoginScreen: () -> Unit
) {
    when (screenState) {
        is AuthHandlerState.Loading -> AuthHandlerContentLoading()
        is AuthHandlerState.Error -> AuthHandlerContentError(onClickTryAgain, goToLoginScreen)
    }
}

@Composable
fun AuthHandlerContentLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 20.dp)
            .semantics { testTag = AuthHandlerTags.LOADING_STATE },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProgressIndicator(
            modifier = Modifier
                .padding()
                .padding(bottom = 20.dp)
        )
        Text(
            text = stringResource(id = R.string.auth_handler_loading),
            style = MaterialTheme.typography.h5
                .copy(textAlign = TextAlign.Center, color = MaterialTheme.colors.onBackground),
        )
    }
}

@Composable
fun AuthHandlerContentError(
    onClickTryAgain: () -> Unit,
    goToLoginScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                testTag = AuthHandlerTags.ERROR_STATE
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TryAgain(
            message = stringResource(id = R.string.auth_handler_error),
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
                .semantics { testTag = AuthHandlerTags.GO_TO_HOME }
        ) {
            Text(
                stringResource(id = R.string.auth_handler_go_to_login),
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AuthHandlerPreview() {
    ImagefyTheme(false) {
        Scaffold() {
            AuthHandlerContent(
                screenState = AuthHandlerState.Loading,
                onClickTryAgain = {},
                goToLoginScreen = {}
            )
        }
    }
}