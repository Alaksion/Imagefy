package br.com.alaksion.core_ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.theme.ImagefyTheme

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
            style = MaterialTheme.typography.body2.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground
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
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                "Try Again",
                style = MaterialTheme.typography.body2
                    .copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.background)
            )
        }
    }

}

@Composable
@Preview(showBackground = true)
fun TryAgainPreview() {
    ImagefyTheme() {
        TryAgain(
            message = "Error",
            icon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Close, null)
                }
            },
            onClick = {}
        )

    }
}