package br.com.alaksion.myapplication.ui.home.authhandler

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import br.com.alaksion.core_ui.components.tryagain.TRY_AGAIN_BUTTON_TAG
import org.junit.Rule
import org.junit.Test

class AuthenticationHandlerScreenTest {

    @get: Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldDisplayLoadingIfViewStateIsLoading() {

        composeRule.setContent {
            AuthHandlerContent(
                screenState = AuthHandlerState.Loading,
                onClickTryAgain = { },
                goToLoginScreen = {}
            )
        }

        composeRule.onNodeWithTag(AuthHandlerTags.LOADING_STATE).assertIsDisplayed()
    }

    @Test
    fun shouldDisplayLoadingIfViewStateIsError() {

        composeRule.setContent {
            AuthHandlerContent(
                screenState = AuthHandlerState.Error,
                onClickTryAgain = { },
                goToLoginScreen = {}
            )
        }

        composeRule.onNodeWithTag(AuthHandlerTags.ERROR_STATE).assertIsDisplayed()
    }

    @Test
    fun shouldTriggerTryAgainCallbackOnClick() {
        var testFlag = false
        val onTryAgainClick = { testFlag = true }

        composeRule.setContent {
            AuthHandlerContent(
                screenState = AuthHandlerState.Error,
                onClickTryAgain = onTryAgainClick,
                goToLoginScreen = {}
            )
        }

        composeRule.onNodeWithTag(TRY_AGAIN_BUTTON_TAG).performClick()
        assert(testFlag)
    }

    @Test
    fun shouldTriggerNavigateToHomeCallbackOnClick() {
        var testFlag = false
        val onTryAgainClick = { testFlag = true }

        composeRule.setContent {
            AuthHandlerContent(
                screenState = AuthHandlerState.Error,
                onClickTryAgain = {},
                goToLoginScreen = onTryAgainClick
            )
        }

        composeRule.onNodeWithTag(AuthHandlerTags.GO_TO_HOME).performClick()
        assert(testFlag)
    }

}