package br.com.alaksion.myapplication.ui.authentication.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.ui.PresentationConstants.REGISTER_URL
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.DimGray
import br.com.alaksion.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                LoginActivityContent()
            }
        }
    }

    private fun navigateToCreateAccount() {
        val uri = Uri.parse(REGISTER_URL)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun openBrowserSignIn() {
        val uri = viewModel.getLoginUrl()
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    @Composable
    fun LoginActivityContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(all = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Red)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Share your best moments with the internet",
                    style = AppTypoGraph.roboto_bold().copy(
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding()
                        .padding(10.dp)
                )
                Text(
                    text = "Bring together pictures of your backyard, pets, hobbies, morning coffee and everything that makes your day brighter.",
                    style = AppTypoGraph.roboto_regular().copy(
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = DimGray
                    )
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            navigateToCreateAccount()
                        }
                        .padding()
                        .padding(bottom = 15.dp)
                ) {
                    Text(
                        "Don't have an account? ",
                        style = AppTypoGraph.roboto_regular()
                            .copy(fontSize = 14.sp, color = DimGray)
                    )
                    Text(
                        "Register", style = AppTypoGraph.roboto_bold()
                            .copy(fontSize = 14.sp)
                    )
                }
                Button(
                    onClick = { openBrowserSignIn() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        "Sign in",
                        style = AppTypoGraph.roboto_bold()
                            .copy(fontSize = 14.sp, color = MaterialTheme.colors.onSurface)
                    )
                }
            }
        }
    }
}