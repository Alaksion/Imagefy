package br.com.alaksion.myapplication.ui.home.login

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get: Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldInvokeNavigateToCreateAccountOnClick() {
        var testTag = false
        val onClick = { testTag = true }

        composeRule.setContent {
            LoginScreenContent(
                navigateToCreateAccount = onClick,
                openBrowserSignIn = {}
            )
        }
        composeRule.onNodeWithTag(LoginTags.REGISTER_BUTTON.name).performClick()
        assert(testTag)
    }

    @Test
    fun shouldInvokeLoginCallBackOnClick() {
        var testTag = false
        val onClick = { testTag = true }

        composeRule.setContent {
            LoginScreenContent(
                navigateToCreateAccount = {},
                openBrowserSignIn = onClick
            )
        }
        composeRule.onNodeWithTag(LoginTags.LOGIN_BUTTON.name).performClick()
        assert(testTag)
    }

}