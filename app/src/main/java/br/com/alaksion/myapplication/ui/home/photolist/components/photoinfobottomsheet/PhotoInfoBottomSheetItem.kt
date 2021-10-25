package br.com.alaksion.myapplication.ui.home.photolist.components.photoinfobottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.ui.theme.ErrorRed
import br.com.alaksion.myapplication.ui.theme.OffWhite

@Composable
fun PhotoInfoBottomSheetItem(
    isError: Boolean = false,
    label: String,
    icon: @Composable () -> Unit,
) {
    val textColor = if (isError) ErrorRed else OffWhite

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        icon()
        Text(
            label,
            style = MaterialTheme.typography.caption.copy(
                color = textColor, fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier
                .padding()
                .padding(top = 5.dp)
        )
    }
}