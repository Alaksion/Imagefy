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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomSheetVisibility
import br.com.alaksion.myapplication.ui.PresentationConstants
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    val bottomSheetState = LocalBottomSheetVisibility.current

    LaunchedEffect(key1 = bottomSheetState.value) {
        bottomSheetState.value = false
    }

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
                text = "Share your best moments with the internet",
                style = MaterialTheme.typography.h5.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground
                ),
                modifier = Modifier
                    .padding()
                    .padding(top = 10.dp, bottom = 5.dp)
            )
            Text(
                text = "Bring together pictures of your backyard, pets, hobbies, morning coffee and everything that makes your day brighter.",
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
                    .padding(bottom = 15.dp),
                text = buildAnnotatedString {
                    withStyle(
                        AppTypoGraph.span_roboto_regular()
                            .copy(
                                fontSize = 14.sp,
                                color = MaterialTheme.colors.onBackground.copy(0.66f)
                            )
                    ) {
                        append("Don't have an account? ")
                    }
                    withStyle(
                        AppTypoGraph.span_roboto_bold()
                            .copy(fontSize = 14.sp)
                    ) {
                        append("Register now!")
                    }
                }
            )
            Button(
                onClick = { openBrowserSignIn() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    "Sign in",
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