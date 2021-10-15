package br.com.alaksion.myapplication.ui.home.searchphotos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@Composable
fun SearchPhotosTopbar(
    drawerState: DrawerState,
    userProfileUrl: String,
    isPreview: Boolean = false
) {
    val scope = rememberCoroutineScope()

    TopAppBar(
        elevation = 1.dp,
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(start = 5.dp, end = 5.dp, top = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlideImage(
                modifier = Modifier
                    .weight(1f)
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .clickable {
                        scope.launch {
                            if (drawerState.isOpen) drawerState.close()
                            else drawerState.open()
                        }
                    },
                imageModel = userProfileUrl,
                contentScale = ContentScale.Fit,
                loading = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmer()
                    )
                },
                failure = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        tint = MaterialTheme.colors.background,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.onBackground)
                            .padding(5.dp)
                    )
                }
            )
            Box(
                modifier = Modifier
                    .background(Color.Red)
                    .weight(3f)
            ) {}
        }
    }
}