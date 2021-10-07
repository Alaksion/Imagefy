package br.com.alaksion.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            style = AppTypoGraph.body_14(),
            modifier = Modifier
                .padding()
                .padding(bottom = 5.dp)
        )
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onBackground
            ),
            modifier = Modifier
                .padding()
                .padding(bottom = 10.dp)
        ) {
            Text(
                "Try Again",
                style = AppTypoGraph.body_14().copy(color = MaterialTheme.colors.background)
            )
        }
    }

}