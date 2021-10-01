package br.com.alaksion.myapplication.ui.home.photolist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.ui.components.ProgressIndicator
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.DimGray
import br.com.alaksion.myapplication.ui.theme.MyApplicationTheme
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer

@Composable
fun PhotoCard(
    photoContent: PhotoResponse,
    modifier: Modifier = Modifier,
    navigateToAuthor: (authorId: String) -> Unit,
    navigateToPhotoDetails: (photoId: String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        PhotoCardHeader(
            userName = photoContent.authorUserName,
            name = photoContent.authorName,
            profileImageUrl = photoContent.authorProfileThumbUrl,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    navigateToAuthor(photoContent.authorUserName)
                }
                .padding(horizontal = 10.dp)
        )
        PhotoCardImage(
            photoContent.photoUrl,
            photoContent.description,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clickable {
                    navigateToPhotoDetails(photoContent.id)
                }
        )
        PhotoCardInfo(
            likes = photoContent.likes,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 15.dp)
        )
    }
}

@Composable
internal fun PhotoCardHeader(
    userName: String,
    name: String,
    profileImageUrl: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 5.dp)
    ) {
        GlideImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp),
            imageModel = profileImageUrl,
            contentScale = ContentScale.Crop,
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
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.background(DimGray)
                )
            }
        )
        Column(
            modifier = Modifier
                .padding()
                .padding(start = 10.dp)
        ) {
            Text(userName, style = AppTypoGraph.body_14_bold())
            Text(name, style = AppTypoGraph.body_12())
        }
    }
}

@Composable
internal fun PhotoCardImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    GlideImage(
        imageModel = imageUrl,
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { ProgressIndicator() }
        },
        failure = {
            Text("Try again button not yet implemented")
        }
    )
}

@Composable
internal fun PhotoCardInfo(
    modifier: Modifier = Modifier,
    likes: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding()
            .padding(top = 5.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .size(20.dp)
                .padding()
                .padding(end = 5.dp)
        )
        Text(
            "$likes likes",
            style = AppTypoGraph.body_14_bold(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoCardPreview() {
    MyApplicationTheme {
        PhotoCard(
            photoContent = PhotoResponse(
                id = "",
                createdAt = "",
                likes = 10,
                description = "",
                authorName = "Daniel Hartman",
                authorUserName = "Dan",
                photoUrl = "",
                authorProfileThumbUrl = "",
            ),
            navigateToAuthor = {},
            navigateToPhotoDetails = {}
        )
    }
}