package br.com.alaksion.myapplication.ui.home.authordetails.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.R
import br.com.alaksion.myapplication.ui.PresentationConstants
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph

@Composable
fun AuthorMediaLinks(
    twitterUser: String,
    instagramUser: String,
    portfolioUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun parseUrl(url: String): Uri {
        return Uri.parse(url)
    }

    Column(modifier = modifier) {
        if (twitterUser.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .padding(bottom = 10.dp)
                    .clickable {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                parseUrl("${PresentationConstants.TWITTER_URL}/$twitterUser")
                            )
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_twitter),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground
                )
                Text(
                    text = "@$twitterUser",
                    style = AppTypoGraph.roboto_bold().copy(fontSize = 14.sp),
                    modifier = Modifier
                        .padding()
                        .padding(start = 10.dp)
                )
            }
        }
        if (instagramUser.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .padding(bottom = 10.dp)
                    .clickable {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                parseUrl("${PresentationConstants.INSTAGRAM_URL}/$instagramUser")
                            )
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_instagram),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground
                )
                Text(
                    text = "@$instagramUser",
                    style = AppTypoGraph.roboto_bold().copy(fontSize = 14.sp),
                    modifier = Modifier
                        .padding()
                        .padding(start = 10.dp)
                )
            }
        }
        if (portfolioUrl.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .padding(bottom = 10.dp)
                    .clickable {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                parseUrl("${PresentationConstants.INSTAGRAM_URL}/$portfolioUrl")
                            )
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .padding()
                        .padding(end = 10.dp)
                )
                Text(
                    text = "Personal Portfolio",
                    style = AppTypoGraph.roboto_bold().copy(fontSize = 14.sp),
                )

            }
        }
    }
}