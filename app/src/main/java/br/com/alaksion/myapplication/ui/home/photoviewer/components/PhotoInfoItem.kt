package br.com.alaksion.myapplication.ui.home.photoviewer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.OffWhite

@Composable
fun PhotoInfoItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    icon: @Composable () -> Unit
) {
    val itemModifier =
        if (onClick != null) modifier else modifier.clickable { onClick?.invoke() }

    Row(
        modifier = itemModifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Text(
            text,
            style = AppTypoGraph.roboto_regular().copy(color = OffWhite, fontSize = 14.sp),
            modifier = Modifier
                .padding()
                .padding(start = 5.dp)
        )
    }
}