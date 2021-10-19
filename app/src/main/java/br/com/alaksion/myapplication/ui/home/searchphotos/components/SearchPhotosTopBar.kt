package br.com.alaksion.myapplication.ui.home.searchphotos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer

@Composable
fun SearchPhotosTopbar(
    toggleDrawer: () -> Unit,
    userProfileUrl: String,
    isPreview: Boolean = false,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSubmitQuery: () -> Unit
) {
    val focusManager = LocalFocusManager.current

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
                Icon(
                    imageVector = Icons.Default.Person,
                    tint = MaterialTheme.colors.onBackground,
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
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
                textStyle = AppTypoGraph.roboto_regular().copy(),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .border(1.dp, MaterialTheme.colors.onBackground)
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSubmitQuery()
                        focusManager.clearFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
            )
        }
    }
}

@Preview()
@Composable
fun SearchPhotosToolbarPreview() {
    ImagefyTheme {
        Scaffold(
            topBar = {
                SearchPhotosTopbar(
                    toggleDrawer = {},
                    userProfileUrl = "",
                    searchQuery = "Search here",
                    onSearchQueryChange = {},
                    onSubmitQuery = {},
                    isPreview = true
                )
            }
        ) {}
    }
}