package br.com.alaksion.myapplication.ui.home.login

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.core_ui.theme.ImagefyTheme
import br.com.alaksion.core_ui.theme.span_roboto_bold
import br.com.alaksion.core_ui.theme.span_roboto_regular
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.ui.PresentationConstants

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val context = LocalContext.current

    fun navigateToCreateAccount() {
        val uri = Uri.parse(PresentationConstants.REGISTER_URL)
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    fun openBrowserSignIn() {
        val uri = viewModel.getLoginUrl()
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    LoginScreenContent(
        navigateToCreateAccount = { navigateToCreateAccount() },
        openBrowserSignIn = { openBrowserSignIn() }
    )

}

@Composable
fun LoginScreenContent(
    navigateToCreateAccount: () -> Unit,
    openBrowserSignIn: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.img_login), null)
            Text(
                text = stringResource(id = R.string.login_title),
                style = MaterialTheme.typography.h5.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground
                ),
                modifier = Modifier
                    .padding()
                    .padding(top = 10.dp, bottom = 5.dp)
            )
            Text(
                text = stringResource(id = R.string.login_description),
                style = MaterialTheme.typography.body2.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground.copy(0.66f)
                )
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        navigateToCreateAccount()
                    }
                    .padding()
                    .padding(bottom = 15.dp)
                    .semantics { testTag = LoginTags.REGISTER_BUTTON.name },
                text = buildAnnotatedString {
                    withStyle(
                        span_roboto_regular()
                            .copy(
                                fontSize = 14.sp,
                                color = MaterialTheme.colors.onBackground.copy(0.66f)
                            )
                    ) {
                        append(stringResource(id = R.string.login_dont_have_account))
                    }
                    append(" ")
                    withStyle(
                        span_roboto_bold()
                            .copy(fontSize = 14.sp)
                    ) {
                        append(stringResource(id = R.string.login_register))
                    }
                }
            )
            Button(
                onClick = { openBrowserSignIn() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .semantics { testTag = LoginTags.LOGIN_BUTTON.name }
            ) {
                Text(
                    stringResource(id = R.string.login_sign_in),
                    style = MaterialTheme.typography.body2
                        .copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onPrimary)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    ImagefyTheme(true) {
        Scaffold() {
            LoginScreenContent({}, {})
        }
    }
}