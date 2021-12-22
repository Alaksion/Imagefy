package br.com.alaksion.myapplication.ui.home.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.components.loaders.ProgressIndicator
import br.com.alaksion.core_ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.common.extensions.safeFlowCollect
import br.com.alaksion.myapplication.domain.model.StoredUser
import com.skydoves.landscapist.rememberDrawablePainter
import kotlinx.coroutines.flow.collect

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    updateUserData: (StoredUser) -> Unit
) {
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = lifeCycleOwner, key2 = viewModel) {
        safeFlowCollect(lifeCycleOwner) {
            viewModel.eventHandler.collect { event ->
                when (event) {
                    is SplashEvent.NavigateToLogin -> navigateToLogin()
                    is SplashEvent.NavigateToHome -> navigateToHome()
                    is SplashEvent.UpdateUserData -> updateUserData(event.user)
                }
            }
        }
    }

    SplashScreenContent()

}

@Composable
fun SplashScreenContent(isPreview: Boolean = false) {
    SplashContentLoading(isPreview)
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
            text = stringResource(id = R.string.app_name),
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
            text = stringResource(id = R.string.splash_loading_content),
            style = MaterialTheme.typography.body2.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SplashScreenPreview() {
    ImagefyTheme(false) {
        Scaffold {
            SplashScreenContent(
                isPreview = true
            )
        }
    }
}