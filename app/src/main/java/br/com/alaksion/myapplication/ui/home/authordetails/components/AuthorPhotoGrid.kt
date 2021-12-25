package br.com.alaksion.myapplication.ui.home.authordetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraRoll
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.alaksion.core_ui.extensions.onBottomReached
import br.com.alaksion.myapplication.domain.model.AuthorPhotos
import br.com.alaksion.myapplication.ui.components.ImageLoader

fun LazyListScope.AuthorPhotoGrid(
    listState: LazyListState,
    authorPhotos: List<AuthorPhotos>,
    getMorePhotos: () -> Unit,
    isPreview: Boolean = false,
    navigateToPhotoViewer: (String) -> Unit
) {
    if (authorPhotos.isEmpty()) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Camera,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .border(
                            BorderStroke(
                                1.dp,
                                color = MaterialTheme.colors.onBackground
                            ),
                            CircleShape
                        )
                )
                Text(
                    "This user has no photos uploaded",
                    style = MaterialTheme.typography.h6.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onBackground
                    )
                )
            }
        }
    } else {
        items(authorPhotos.chunked(3)) { photoChunk ->
            listState.onBottomReached(offset = 3) {
                getMorePhotos()
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                for (photo in photoChunk) {
                    if (isPreview) {
                        Icon(imageVector = Icons.Default.CameraRoll,
                            contentDescription = null,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .border(1.dp, color = MaterialTheme.colors.onBackground)
                                .clickable {
                                    navigateToPhotoViewer(photo.photoId)
                                })

                    } else {
                        ImageLoader(
                            backgroundColor = photo.color,
                            imageUrl = photo.photoUrl,
                            modifier =
                            Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .border(1.dp, color = MaterialTheme.colors.background)
                                .clickable {
                                    navigateToPhotoViewer(photo.photoId)
                                }
                        )
                    }
                }
            }
        }
    }
}