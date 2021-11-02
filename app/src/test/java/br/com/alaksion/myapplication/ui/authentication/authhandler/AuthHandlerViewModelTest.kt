package br.com.alaksion.myapplication.ui.authentication.authhandler

import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.domain.usecase.GetCurrentUsernameUseCase
import br.com.alaksion.myapplication.domain.usecase.StoreAuthTokenUseCase
import br.com.alaksion.myapplication.domain.usecase.ValidateLoginUseCase
import br.com.alaksion.myapplication.testdata.AuthorProfileTestData
import br.com.alaksion.myapplication.testdata.UserNameTestData
import br.com.alaksion.myapplication.testdata.ValidateLoginTestData
import br.com.alaksion.myapplication.ui.home.authhandler.AuthHandlerViewModel
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import br.com.alaksion.myapplication.utils.ImagefyBaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class AuthHandlerViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: AuthHandlerViewModel
    private val validateLoginUseCase: ValidateLoginUseCase = mockk(relaxed = true)
    private val storeAuthTokenUseCase: StoreAuthTokenUseCase = mockk(relaxed = true)
    private val getCurrentUsernameUseCase: GetCurrentUsernameUseCase = mockk(relaxed = true)
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase = mockk(relaxed = true)
    private lateinit var userData: CurrentUserData


    override fun setUp() {
        userData = CurrentUserData()
        viewModel = AuthHandlerViewModel(
            validateLoginUseCase = validateLoginUseCase,
            storeAuthTokenUseCase = storeAuthTokenUseCase,
            getCurrentUsernameUseCase = getCurrentUsernameUseCase,
            getAuthorProfileUseCase = getAuthorProfileUseCase,
            userData = userData
        )
    }

    @Test
    fun `should validate login with given authCode and store it in local preferences`() =
        runBlockingTest {
            coEvery { validateLoginUseCase(any()) } returns Source.Success(ValidateLoginTestData.DOMAIN_RESPONSE)
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns Source.Success(UserNameTestData.DOMAIN_RESPONSE)
            coEvery { getAuthorProfileUseCase(any()) } returns Source.Success(AuthorProfileTestData.DOMAIN_RESPONSE)

            viewModel.authenticateUser("authCode")

            checkCalls(
                validateLoginCalls = 1,
                storeAuthTokenCalls = 1,
                getAuthorProfileCalls = 1,
                getUsernameCalls = 1
            )

            assertNotNull(viewModel.handleNavigationSuccess.value?.getContentIfNotHandled())
        }

    @Test
    fun `should set viewstate to error if authcode is null`() =
        runBlockingTest {
            coEvery { validateLoginUseCase(any()) } returns Source.Error(NetworkError("", 500))

            viewModel.authenticateUser(null)

            checkCalls(
                validateLoginCalls = 0,
                storeAuthTokenCalls = 0,
                getAuthorProfileCalls = 0,
                getUsernameCalls = 0
            )

            assertTrue(viewModel.authenticationState.value is ViewState.Error)
        }

    @Test
    fun `should set viewstate to error if validate login fails`() =
        runBlockingTest {
            coEvery { validateLoginUseCase(any()) } returns Source.Error(NetworkError("", 500))

            viewModel.authenticateUser("authCode")

            checkCalls(
                validateLoginCalls = 1,
                storeAuthTokenCalls = 0,
                getAuthorProfileCalls = 0,
                getUsernameCalls = 0
            )

            assertTrue(viewModel.authenticationState.value is ViewState.Error)
        }

    @Test
    fun `should set viewstate to error if validate login succeeds but returns a null object`() =
        runBlockingTest {
            coEvery { validateLoginUseCase(any()) } returns Source.Success(null)

            viewModel.authenticateUser("authCode")

            checkCalls(
                validateLoginCalls = 1,
                storeAuthTokenCalls = 0,
                getAuthorProfileCalls = 0,
                getUsernameCalls = 0
            )
            assertTrue(viewModel.authenticationState.value is ViewState.Error)
        }

    @Test
    fun `should set viewState to error if get current username fails`() =
        runBlockingTest {
            coEvery { validateLoginUseCase(any()) } returns Source.Success(ValidateLoginTestData.DOMAIN_RESPONSE)
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns Source.Error(NetworkError("", 500))

            viewModel.authenticateUser("authCode")

            checkCalls(
                validateLoginCalls = 1,
                storeAuthTokenCalls = 1,
                getUsernameCalls = 1,
                getAuthorProfileCalls = 0
            )

            assertTrue(viewModel.authenticationState.value is ViewState.Error)
        }

    @Test
    fun `should set viewState to error if get current username succeeds but returns a null object`() =
        runBlockingTest {
            coEvery { validateLoginUseCase(any()) } returns Source.Success(ValidateLoginTestData.DOMAIN_RESPONSE)
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns Source.Success(null)

            viewModel.authenticateUser("authCode")

            checkCalls(
                validateLoginCalls = 1,
                storeAuthTokenCalls = 1,
                getUsernameCalls = 1,
                getAuthorProfileCalls = 0
            )

            assertTrue(viewModel.authenticationState.value is ViewState.Error)
        }

    @Test
    fun `should set viewState to error if get author profile fails`() =
        runBlockingTest {
            coEvery { validateLoginUseCase(any()) } returns Source.Success(ValidateLoginTestData.DOMAIN_RESPONSE)
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns Source.Success(UserNameTestData.DOMAIN_RESPONSE)
            coEvery { getAuthorProfileUseCase(any()) } returns Source.Error(NetworkError("", 500))

            viewModel.authenticateUser("authCode")

            checkCalls(
                validateLoginCalls = 1,
                storeAuthTokenCalls = 1,
                getAuthorProfileCalls = 1,
                getUsernameCalls = 1
            )

            assertTrue(viewModel.authenticationState.value is ViewState.Error)
        }

    @Test
    fun `should set viewState to error if get author profile succeeds but returns a null object`() =
        runBlockingTest {
            coEvery { validateLoginUseCase(any()) } returns Source.Success(ValidateLoginTestData.DOMAIN_RESPONSE)
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns Source.Success(UserNameTestData.DOMAIN_RESPONSE)
            coEvery { getAuthorProfileUseCase(any()) } returns Source.Success(null)

            viewModel.authenticateUser("authCode")

            checkCalls(
                validateLoginCalls = 1,
                storeAuthTokenCalls = 1,
                getAuthorProfileCalls = 1,
                getUsernameCalls = 1
            )

            assertTrue(viewModel.authenticationState.value is ViewState.Error)
        }

    private fun checkCalls(
        validateLoginCalls: Int,
        storeAuthTokenCalls: Int,
        getUsernameCalls: Int,
        getAuthorProfileCalls: Int
    ) {
        coVerify(exactly = validateLoginCalls) { validateLoginUseCase("authCode") }
        coVerify(exactly = storeAuthTokenCalls) { storeAuthTokenUseCase(ValidateLoginTestData.DOMAIN_RESPONSE.accessToken) }
        coVerify(exactly = getUsernameCalls) { getCurrentUsernameUseCase() }
        coVerify(exactly = getAuthorProfileCalls) { getAuthorProfileUseCase(UserNameTestData.DOMAIN_RESPONSE.username) }
        confirmVerified(validateLoginUseCase)
        confirmVerified(storeAuthTokenUseCase)
        confirmVerified(getCurrentUsernameUseCase)
        confirmVerified(getAuthorProfileUseCase)
    }

}