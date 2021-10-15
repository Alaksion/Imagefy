package br.com.alaksion.myapplication.ui.home.searchphotos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.ui.theme.LightGray
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer

@Composable
fun SearchPhotosTopbar(
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
    isPreview: Boolean = false,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    TopAppBar(
        elevation = 1.dp,
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(start = 5.dp, end = 5.dp, top = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isPreview) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
            } else {
                GlideImage(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .clickable { toggleDrawer() },
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
            }

            BasicTextField(
                value = searchQuery,
                onValueChange = { value -> onSearchQueryChange(value) },
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(LightGray)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPhotosToolbarPreview() {
    ImagefyTheme {
        Scaffold(
            topBar = {
                SearchPhotosTopbar(
                    toggleDrawer = {}, userProfileUrl = ""
                )
            }
        ) {}
    }
}