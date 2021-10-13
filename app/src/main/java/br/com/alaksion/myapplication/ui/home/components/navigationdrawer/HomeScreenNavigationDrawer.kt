package br.com.alaksion.myapplication.ui.home.components.navigationdrawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DrawerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alaksion.myapplication.common.extensions.formatNumber
import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import br.com.alaksion.myapplication.ui.theme.AppTypoGraph
import br.com.alaksion.myapplication.ui.theme.DimGray

@Composable
fun HomeScreenNavigationDrawer(
    drawerState: DrawerState,
    userData: CurrentUserData,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ModalDrawer(
        drawerState = drawerState,
        drawerBackgroundColor = MaterialTheme.colors.background,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            Column(modifier = modifier.padding(horizontal = 20.dp)) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
                Text(
                    userData.name.toString(),
                    style = AppTypoGraph.roboto_bold().copy(fontSize = 16.sp)
                )
                Text(
                    userData.userName.toString(),
                    style = AppTypoGraph.roboto_regular().copy(fontSize = 14.sp, color = DimGray),
                    modifier = Modifier
                        .padding()
                        .padding(bottom = 5.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(buildAnnotatedString {
                        withStyle(AppTypoGraph.span_roboto_bold().copy(fontSize = 12.sp)) {
                            append(userData.followingCount.handleOptional().formatNumber())
                        }
                        append(" ")
                        withStyle(
                            AppTypoGraph.span_roboto_regular()
                                .copy(fontSize = 12.sp, color = DimGray)
                        ) {
                            append("Following")
                        }
                    })
                    Text(buildAnnotatedString {
                        withStyle(AppTypoGraph.span_roboto_bold().copy(fontSize = 12.sp)) {
                            append(userData.followersCount.handleOptional().formatNumber())
                        }
                        append(" ")
                        withStyle(
                            AppTypoGraph.span_roboto_regular()
                                .copy(fontSize = 12.sp, color = DimGray)
                        ) {
                            append("Followers")
                        }
                    })
                }
            }
        }
    ) {
        content()
    }
}