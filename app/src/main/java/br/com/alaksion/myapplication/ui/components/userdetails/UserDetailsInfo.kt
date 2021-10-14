package br.com.alaksion.myapplication.ui.components.userdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph

@Composable
fun UserDetailsInfo(
    name: String,
    bio: String,
    twitterUser: String,
    instagramUser: String,
    portfolioUrl: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            name,
            style = AppTypoGraph.roboto_bold().copy(fontSize = 14.sp),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
        )
        if (bio.isNotEmpty()) {
            Text(
                bio,
                style = AppTypoGraph.roboto_regular().copy(fontSize = 14.sp),
            )
        }
        AuthorMediaLinks(
            twitterUser = twitterUser,
            instagramUser = instagramUser,
            portfolioUrl = portfolioUrl,
            modifier = Modifier
                .padding(top = 10.dp)
        )
    }
}