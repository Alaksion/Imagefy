package br.com.alaksion.myapplication.ui.authentication.authhandler

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
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
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.ui.authentication.login.LoginViewModel
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.home.HomeActivity
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class AuthenticationHandlerAct : AppCompatActivity() {

    private val viewModel by viewModels<AuthHandlerViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpObservers()

        setContent {
            AuthHandlerContent(viewModel.authenticationResult.value)
        }

    }

    private fun setUpObservers() {
        with(viewModel) {
            handleNavigationSuccess.observeEvent(this@AuthenticationHandlerAct) {
                loginViewModel.setAuthResult()
                HomeActivity.start(this@AuthenticationHandlerAct)
                finishAffinity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val authCode = intent.data?.encodedQuery?.substring(5)
        viewModel.authenticateUser(authCode)
    }

    @Composable
    fun AuthHandlerContent(
        screenState: ViewState<Unit>
    ) {
        MyApplicationTheme {
            Scaffold {
                when (screenState) {
                    is ViewState.Loading, is ViewState.Ready, is ViewState.Idle ->
                        AuthHandlerContentLoading()
                    is ViewState.Error -> AuthHandlerContentError()
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
    fun AuthHandlerContentError() {
        Text("deu erro")
    }

}