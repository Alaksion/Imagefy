package br.com.alaksion.myapplication.ui.components.userdetails.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.common.extensions.formatNumber
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer

@Composable
fun UserDetailsHeader(
    profileImageUrl: String,
    photoCount: Int,
    followersCount: Int,
    followingCount: Int,
    modifier: Modifier = Modifier,
    isPreview: Boolean = false,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isPreview) {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .weight(2.0f)
                    .background(Color.Red)
            )
        } else {
            GlideImage(
                contentScale = ContentScale.Crop,
                imageModel = profileImageUrl,
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .weight(2.0f),
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
                        tint = MaterialTheme.colors.onBackground,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }
            )
        }
        AuthorHeaderItem(
            value = photoCount.formatNumber(),
            label = "Posts",
            modifier = Modifier
                .weight(1.0f)
        )
        AuthorHeaderItem(
            value = followersCount.formatNumber(),
            label = stringResource(id = R.string.followers),
            modifier = Modifier
                .weight(1.0f)
                .padding()
                .padding(start = 10.dp)
        )
        AuthorHeaderItem(
            value = followingCount.formatNumber(),
            label = stringResource(id = R.string.following),
            modifier = Modifier
                .weight(1.0f)
                .padding()
                .padding(start = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorDetailsHeaderPreview() {
    ImagefyTheme {
        UserDetailsHeader(
            profileImageUrl = "",
            photoCount = 100,
            followersCount = 120,
            followingCount = 130,
            isPreview = true,
        )
    }
}