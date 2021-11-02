package br.com.alaksion.myapplication.ui.home.components.navigationdrawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.common.extensions.formatNumber
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.DimGray
import br.com.alaksion.myapplication.ui.theme.ImagefyTheme
import br.com.alaksion.myapplication.ui.theme.LightGray
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer

@Composable
fun HomeScreenNavigationDrawer(
    drawerState: DrawerState,
    userData: CurrentUserData,
    modifier: Modifier = Modifier,
    navigateToAuthorProfile: () -> Unit,
    isPreviewMode: Boolean = false,
    onLogoutClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val horizontalPadding = 20.dp

    ModalDrawer(
        drawerState = drawerState,
        drawerBackgroundColor = MaterialTheme.colors.background,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            Column(
                modifier = modifier
                    .padding(vertical = 10.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    if (isPreviewMode) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = horizontalPadding)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                    } else {
                        Box(
                            Modifier
                                .padding()
                                .padding(horizontal = horizontalPadding)
                        ) {
                            GlideImage(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red),
                                imageModel = userData.profileImageUrl,
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
                    }

                    Text(
                        userData.name,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(
                            start = horizontalPadding,
                            end = horizontalPadding,
                            top = 5.dp
                        )
                    )
                    Text(
                        userData.userName,
                        style = MaterialTheme.typography.body1.copy(color = DimGray),
                        modifier = Modifier
                            .padding()
                            .padding(horizontal = horizontalPadding)
                    )
                    FollowingData(
                        followersCount = userData.followersCount,
                        followingCount = userData.followingCount,
                        modifier = Modifier.padding(
                            horizontal = horizontalPadding,
                            vertical = 15.dp
                        )
                    )
                    Divider(color = LightGray, modifier = Modifier.fillMaxWidth())
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(48.dp)
                            .padding(horizontal = horizontalPadding)
                            .fillMaxWidth()
                            .clickable {
                                navigateToAuthorProfile()
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground,
                            modifier = Modifier
                                .padding()
                                .padding(end = 10.dp)
                        )
                        Text(
                            "Profile",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                        .clickable {
                            onLogoutClick()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        modifier = Modifier
                            .padding()
                            .padding(end = 5.dp)
                    )
                    Text(
                        "Logout",
                        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    ) {
        content()
    }
}

@Composable
fun FollowingData(followersCount: Int, followingCount: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(buildAnnotatedString {
            withStyle(AppTypoGraph.span_roboto_bold().copy(fontSize = 14.sp)) {
                append(followingCount.formatNumber())
            }
            append(" ")
            withStyle(
                AppTypoGraph.span_roboto_regular()
                    .copy(fontSize = 14.sp, color = DimGray)
            ) {
                append("Following")
            }
        })
        Text(buildAnnotatedString {
            withStyle(AppTypoGraph.span_roboto_bold().copy(fontSize = 14.sp)) {
                append(followersCount.formatNumber())
            }
            append(" ")
            withStyle(
                AppTypoGraph.span_roboto_regular()
                    .copy(fontSize = 14.sp, color = DimGray)
            ) {
                append("Followers")
            }
        })
    }
}


@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    ImagefyTheme {
        HomeScreenNavigationDrawer(
            isPreviewMode = true,
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            userData = CurrentUserData(
                name = "JohenDoe",
                userName = "SuperJohn",
                followingCount = 100,
                followersCount = 140,
                profileImageUrl = ""
            ),
            navigateToAuthorProfile = {},
            onLogoutClick = {},
            content = {}
        )
    }
}