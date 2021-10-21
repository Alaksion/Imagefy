package br.com.alaksion.myapplication.ui.authentication.authhandler

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.ui.authentication.login.LoginActivity
import br.com.alaksion.myapplication.ui.authentication.login.LoginViewModel
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.home.HomeActivity
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class AuthenticationHandlerAct : AppCompatActivity() {

    private val viewModel by viewModels<AuthHandlerViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()
    private val authCode = intent.data?.encodedQuery?.substring(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpObservers()

        setContent {
            AuthHandlerContent(
                screenState = viewModel.authenticationResult.value,
                goToLoginScreen = { LoginActivity.start(this) },
                onClickTryAgain = { viewModel.authenticateUser(authCode) }
            )
        }

    }

    private fun setUpObservers() {
        with(viewModel) {
            handleNavigationSuccess.observeEvent(this@AuthenticationHandlerAct) { authSucceeded ->
                if (authSucceeded) {
                    loginViewModel.setAuthResult()
                    HomeActivity.start(this@AuthenticationHandlerAct)
                    finish()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.authenticateUser(authCode)
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
                style = AppTypoGraph.roboto_bold()
                    .copy(fontSize = 24.sp, textAlign = TextAlign.Center),
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

}