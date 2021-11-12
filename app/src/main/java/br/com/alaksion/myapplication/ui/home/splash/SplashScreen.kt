package br.com.alaksion.myapplication.ui.home.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.ui.providers.LocalBottomSheetVisibility
import br.com.alaksion.myapplication.common.utils.observeEvent
import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.myapplication.ui.components.TryAgain
import br.com.alaksion.myapplication.ui.components.loaders.ProgressIndicator
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import com.skydoves.landscapist.rememberDrawablePainter

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    updateUserData: (StoredUser) -> Unit
) {
    val bottomSheetState = LocalBottomSheetVisibility.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = true) {
        bottomSheetState.value = false
        onDispose {
            bottomSheetState.value = true
        }
    }

    DisposableEffect(key1 = lifeCycleOwner) {
        viewModel.isUserLogged.observeEvent(lifeCycleOwner) { isUserLogged ->
            if (isUserLogged) navigateToHome()
            else navigateToLogin()
        }
        viewModel.currentUserData.observeEvent(lifeCycleOwner) {
            updateUserData(it)
        }

        onDispose {
            viewModel.currentUserData.removeObservers(lifeCycleOwner)
            viewModel.isUserLogged.removeObservers(lifeCycleOwner)
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