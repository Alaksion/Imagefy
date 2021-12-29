package br.com.alaksion.myapplication.ui.home.photolist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.rememberDrawablePainter

@Composable
fun PhotoListTopBar(
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
    isPreview: Boolean = false
) {
    val context = LocalContext.current

    TopAppBar(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(start = 5.dp, end = 5.dp, top = 5.dp),
    ) {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val appIcon = context.packageManager.getApplicationIcon(context.applicationInfo)
                GlideImage(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .clickable { toggleDrawer() },
                    imageModel = userProfileUrl,
                    contentScale = ContentScale.Fit,
                    loading = {
                        Box(
                            Modifier
                                .fillMaxSize()
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
                Image(
                    painter = rememberDrawablePainter(drawable = appIcon),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}
