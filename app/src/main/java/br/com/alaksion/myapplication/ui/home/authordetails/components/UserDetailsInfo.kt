package br.com.alaksion.myapplication.ui.home.authordetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
            ),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
        )
        if (bio.isNotEmpty()) {
            Text(
                bio,
                style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.onBackground),
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