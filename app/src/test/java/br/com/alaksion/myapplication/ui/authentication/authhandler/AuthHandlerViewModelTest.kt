package br.com.alaksion.myapplication.ui.authentication.authhandler

import app.cash.turbine.test
import br.com.alaksion.myapplication.domain.model.Auth
import br.com.alaksion.myapplication.domain.model.Author
import br.com.alaksion.myapplication.domain.model.CurrentUser
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.domain.usecase.GetCurrentUsernameUseCase
import br.com.alaksion.myapplication.domain.usecase.StoreUserDataUseCase
import br.com.alaksion.myapplication.domain.usecase.ValidateLoginUseCase
import br.com.alaksion.myapplication.testdata.AuthorProfileTestData
import br.com.alaksion.myapplication.testdata.UserNameTestData
import br.com.alaksion.myapplication.testdata.ValidateLoginTestData
import br.com.alaksion.myapplication.ui.home.authhandler.AuthHandlerEvents
import br.com.alaksion.myapplication.ui.home.authhandler.AuthHandlerViewModel
import br.com.alaksion.myapplication.ui.model.ViewState
import br.com.alaksion.myapplication.utils.ImagefyBaseViewModelTest
import br.com.alaksion.network.NetworkError
import br.com.alaksion.network.Source
import br.com.alaksion.network.client.domain.usecase.StoreAuthTokenUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class AuthHandlerViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: AuthHandlerViewModel
    private val validateLoginUseCase: ValidateLoginUseCase = mockk(relaxed = true)
    private val storeAuthTokenUseCase: StoreAuthTokenUseCase = mockk(relaxed = true)
    private val getCurrentUsernameUseCase: GetCurrentUsernameUseCase = mockk(relaxed = true)
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase = mockk(relaxed = true)
    private val storeUserDataUseCase: StoreUserDataUseCase = mockk(relaxed = true)

    override fun setUp() {
        viewModel = AuthHandlerViewModel(
            validateLoginUseCase = validateLoginUseCase,
            storeAuthTokenUseCase = storeAuthTokenUseCase,
            getCurrentUsernameUseCase = getCurrentUsernameUseCase,
            getAuthorProfileUseCase = getAuthorProfileUseCase,
            storeUserDataUseCase = storeUserDataUseCase
        )
    }

    @Test
    fun `should validate login with given authCode and store it in local preferences`() =
        runBlockingTest {

            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        ValidateLoginTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns flow {
                emit(
                    Source.Success(
                        UserNameTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { getAuthorProfileUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        AuthorProfileTestData.DOMAIN_RESPONSE
                    )
                )
            }

            viewModel.events.test {

                viewModel.authenticateUser("authCode")

                checkCalls(
                    validateLoginCalls = 1,
                    storeAuthTokenCalls = 1,
                    getAuthorProfileCalls = 1,
                    getUsernameCalls = 1
                )

                val emission = awaitItem()
                assertTrue(emission is AuthHandlerEvents.UpdateUserData)
                cancelAndIgnoreRemainingEvents()

            }
        }

    @Test
    fun `Should update user data if login succeeds`() =
        runBlockingTest {

            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        ValidateLoginTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns flow {
                emit(
                    Source.Success(
                        UserNameTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { getAuthorProfileUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        AuthorProfileTestData.DOMAIN_RESPONSE
                    )
                )
            }

            viewModel.events.test {
                viewModel.authenticateUser("authCode")

                val emission = awaitItem()
                assertTrue(emission is AuthHandlerEvents.UpdateUserData)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Should navigate to home  if login succeeds`() =
        runBlockingTest {

            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        ValidateLoginTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns flow {
                emit(
                    Source.Success(
                        UserNameTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { getAuthorProfileUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        AuthorProfileTestData.DOMAIN_RESPONSE
                    )
                )
            }

            viewModel.events.test {
                viewModel.authenticateUser("authCode")
                awaitItem()
                val emission = awaitItem()
                assertTrue(emission is AuthHandlerEvents.NavigateToSuccess)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `should set viewstate to error if authcode is null`() =
        runBlockingTest {
            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(
                    Source.Error<Auth>(
                        NetworkError(
                            "",
                            500
                        )
                    )
                )
            }

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
            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(Source.Error<Auth>(NetworkError("", 500)))
            }

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
            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(Source.Success(null))
            }

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
            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        ValidateLoginTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns flow {
                emit(
                    Source.Error<CurrentUser>(
                        NetworkError(
                            "",
                            500
                        )
                    )
                )
            }

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
            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        ValidateLoginTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns flow { emit(Source.Success(null)) }

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
            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        ValidateLoginTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns flow {
                emit(
                    Source.Success(
                        UserNameTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { getAuthorProfileUseCase(any()) } returns flow {
                emit(
                    Source.Error<Author>(
                        NetworkError(
                            "",
                            500
                        )
                    )
                )
            }

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
            coEvery { validateLoginUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        ValidateLoginTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { storeAuthTokenUseCase(any()) } returns Unit
            coEvery { getCurrentUsernameUseCase() } returns flow {
                emit(
                    Source.Success(
                        UserNameTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { getAuthorProfileUseCase(any()) } returns flow { emit(Source.Success(null)) }

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