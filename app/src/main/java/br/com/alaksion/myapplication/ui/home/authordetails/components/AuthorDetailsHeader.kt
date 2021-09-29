package br.com.alaksion.myapplication.ui.home.authordetails.components

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
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.data.model.author.components.AuthorHeaderItem
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer

@Composable
fun AuthorDetailsHeader(
    profileImageUrl: String,
    photoCount: Int,
    followersCount: Int,
    followingCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
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
        AuthorHeaderItem(
            value = photoCount.toString(),
            label = "Posts",
            modifier = Modifier
                .weight(1.0f)
                .padding()
                .padding(start = 20.dp)
        )
        AuthorHeaderItem(
            value = followersCount.toString(),
            label = "Followers",
            modifier = Modifier
                .weight(1.0f)
                .padding()
                .padding(start = 10.dp)
        )
        AuthorHeaderItem(
            value = followingCount.toString(),
            label = "Following",
            modifier = Modifier
                .weight(1.0f)
                .padding()
                .padding(start = 10.dp)
        )
    }
}