package br.com.alaksion.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph

@Composable
fun TryAgain(
    message: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Text(
            message,
            style = AppTypoGraph.roboto_regular()
                .copy(
                    textAlign = TextAlign.Center, fontSize = 14.sp
                ),
            modifier = Modifier
                .padding()
                .padding(vertical = 10.dp)
        )
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Try Again",
                style = AppTypoGraph.roboto_regular()
                    .copy(color = MaterialTheme.colors.background, fontSize = 14.sp)
            )
        }
    }

}