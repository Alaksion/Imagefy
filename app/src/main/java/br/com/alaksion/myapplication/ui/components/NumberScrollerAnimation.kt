package br.com.alaksion.myapplication.ui.components

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalAnimationApi
@Composable
fun NumberScrollerAnimation(
    value: Int,
    modifier: Modifier = Modifier,
    content: @Composable (currentValue: String) -> Unit,
) {
    AnimatedContent(modifier = modifier, targetState = value, transitionSpec = {
        /* If the image was liked "scroll up" the like value*/
        if (targetState > initialState) {
            slideInVertically({ height -> height }) + fadeIn() with
                    slideOutVertically({ height -> -height }) + fadeOut()
        }
        /* If the image was unliked "scroll down" the like value*/
        else {
            slideInVertically({ height -> -height }) + fadeIn() with
                    slideOutVertically({ height -> height }) + fadeOut()
        }.using(SizeTransform(clip = false))
    }) { targetCount ->
        content(targetCount.toString())
    }
}