package br.com.alaksion.myapplication.ui.home.photoviewer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.alaksion.core_ui.components.tryagain.TRY_AGAIN_BUTTON_TAG
import br.com.alaksion.myapplication.domain.model.PhotoDetail
import org.junit.Rule
import org.junit.Test

@ExperimentalAnimationApi
class PhotoViewerTest {

    @get: Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowLoaderWhenScreenStateIsLoading() {
        composeRule.setContent {
            PhotoViewerScreenContent(
                photoState = PhotoViewerState.Loading,
                popBackStack = { true },
                onRateImage = {},
                onClickTryAgain = {}
            )
        }
        composeRule.onNodeWithTag(PhotoViewerTags.LOADING.name).assertIsDisplayed()
    }

    @Test
    fun shouldShowErrorWhenScreenStateIsError() {
        composeRule.setContent {
            PhotoViewerScreenContent(
                photoState = PhotoViewerState.Error,
                popBackStack = { true },
                onRateImage = {},
                onClickTryAgain = {}
            )
        }
        composeRule.onNodeWithTag(PhotoViewerTags.ERROR.name).assertIsDisplayed()
    }

    @Test
    fun shouldInvokeTryAgainCallbackOnClick() {
        var testFlag = false
        val onClick = { testFlag = true }

        composeRule.setContent {
            PhotoViewerScreenContent(
                photoState = PhotoViewerState.Error,
                popBackStack = { true },
                onRateImage = {},
                onClickTryAgain = onClick
            )
        }
        composeRule.onNodeWithTag(TRY_AGAIN_BUTTON_TAG).performClick()
        assert(testFlag)
    }

    @Test
    fun shouldInvokePopBackStackOnClick() {
        var testFlag = false
        val onClick: () -> Boolean = {
            testFlag = true
            true
        }

        composeRule.setContent {
            PhotoViewerScreenContent(
                photoState = PhotoViewerState.Error,
                popBackStack = onClick,
                onRateImage = {},
                onClickTryAgain = {}
            )
        }
        composeRule.onNodeWithTag(PhotoViewerTags.BACK_BUTTON.name).performClick()
        assert(testFlag)
    }

    @Test
    fun shouldHideStatsBarOnClick() {
        composeRule.setContent {
            composeRule.setContent {
                PhotoViewerScreenContent(
                    photoState = PhotoViewerState.Ready(
                        PhotoDetail(
                            "awdawdawd",
                            "",
                            "",
                            "",
                            "",
                            1,
                            1,
                            true,
                            "#ffffff"
                        )
                    ),
                    popBackStack = { true },
                    onRateImage = {},
                    onClickTryAgain = {}
                )
            }
        }
        composeRule.onNodeWithTag(PhotoViewerTags.IMAGE_LOADER.name).performClick()
        composeRule.onNodeWithTag(PhotoViewerTags.STATS_BAR.name).assertIsNotDisplayed()
    }

    @Test
    fun shouldStartWithStatsBarVisible() {
        composeRule.setContent {
            PhotoViewerScreenContent(
                photoState = PhotoViewerState.Ready(
                    PhotoDetail(
                        "awdawdawd",
                        "",
                        "",
                        "",
                        "",
                        1,
                        1,
                        true,
                        "#ffffff"
                    )
                ),
                popBackStack = { true },
                onRateImage = {},
                onClickTryAgain = {}
            )
        }
        composeRule.onNodeWithTag(PhotoViewerTags.STATS_BAR.name).assertIsDisplayed()
    }

    @Test
    fun shouldInvokeRateImageOnClick() {
        var testFlag = false
        val onClick: (Boolean) -> Unit = {
            testFlag = true
        }

        composeRule.setContent {
            PhotoViewerScreenContent(
                photoState = PhotoViewerState.Ready(
                    PhotoDetail(
                        "awdawdawd",
                        "",
                        "",
                        "",
                        "",
                        1,
                        1,
                        true,
                        "#ffffff"
                    )
                ),
                popBackStack = { true },
                onRateImage = onClick,
                onClickTryAgain = {}
            )
        }
        composeRule.onNodeWithTag(PhotoViewerTags.RATE_IMAGE.name).performClick()
        assert(testFlag)
    }

}