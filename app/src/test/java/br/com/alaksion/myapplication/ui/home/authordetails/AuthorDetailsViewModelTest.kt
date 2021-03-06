package br.com.alaksion.myapplication.ui.home.authordetails

import app.cash.turbine.test
import br.com.alaksion.myapplication.domain.model.Author
import br.com.alaksion.myapplication.domain.model.AuthorPhotos
import br.com.alaksion.myapplication.domain.usecase.GetAuthorPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.testdata.AuthorPhotosTestData
import br.com.alaksion.myapplication.testdata.AuthorProfileTestData
import br.com.alaksion.myapplication.ui.model.ViewState
import br.com.alaksion.myapplication.utils.ImagefyBaseViewModelTest
import br.com.alaksion.network.model.NetworkError
import br.com.alaksion.network.model.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class AuthorDetailsViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: AuthorDetailsViewModel
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase = mockk(relaxed = true)
    private val getAuthorPhotoUseCase: GetAuthorPhotosUseCase = mockk(relaxed = true)

    /*
    * This method must be called instead of a regular @Before annotated function because the init block
    * generated inside the @Before block cannot be accessed in test runtime
    * */
    private fun setupViewModel() {
        viewModel =
            AuthorDetailsViewModel("username", getAuthorProfileUseCase, getAuthorPhotoUseCase)
    }

    @Test
    fun `Should get author profile and first page of author photos`() = runBlocking {
        coEvery { getAuthorProfileUseCase(any()) } returns flow {
            emit(
                Source.Success(
                    AuthorProfileTestData.DOMAIN_RESPONSE
                )
            )
        }
        coEvery {
            getAuthorPhotoUseCase(
                any(),
                any()
            )
        } returns flow { emit(Source.Success(AuthorPhotosTestData.DOMAIN_RESPONSE)) }

        setupViewModel()

        coVerify(exactly = 1) {
            getAuthorPhotoUseCase(
                "username",
                1
            )
        }
        coVerify(exactly = 1) { getAuthorProfileUseCase("username") }
        confirmVerified(getAuthorProfileUseCase)
        confirmVerified(getAuthorPhotoUseCase)

        assertEquals(
            AuthorPhotosTestData.DOMAIN_RESPONSE,
            (viewModel.authorPhotosState.value as ViewState.Ready).data.toList()
        )
        assertEquals(
            AuthorProfileTestData.DOMAIN_RESPONSE,
            (viewModel.authorData.value as ViewState.Ready).data
        )
    }

    @Test
    fun `Should set viewState to error if get author profile fails`() = runBlocking {
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
        setupViewModel()

        coVerify(exactly = 0) { getAuthorPhotoUseCase(any(), any()) }
        coVerify(exactly = 1) { getAuthorProfileUseCase("username") }
        confirmVerified(getAuthorProfileUseCase)
        confirmVerified(getAuthorPhotoUseCase)

        assertTrue(viewModel.authorData.value is ViewState.Error)
    }

    @Test
    fun `Should set viewState to error if get author profile succeeds but returns a null object`() =
        runBlocking {
            coEvery { getAuthorProfileUseCase(any()) } returns flow { emit(Source.Success(null)) }

            setupViewModel()

            coVerify(exactly = 0) { getAuthorPhotoUseCase(any(), any()) }
            coVerify(exactly = 1) { getAuthorProfileUseCase("username") }
            confirmVerified(getAuthorProfileUseCase)
            confirmVerified(getAuthorPhotoUseCase)

            assertTrue(viewModel.authorData.value is ViewState.Error)
        }

    @Test
    fun `Should set photos viewstate to error when get author photos fails`() = runBlocking {
        coEvery { getAuthorProfileUseCase(any()) } returns flow {
            emit(
                Source.Success(
                    AuthorProfileTestData.DOMAIN_RESPONSE
                )
            )
        }
        coEvery { getAuthorPhotoUseCase(any(), any()) } returns flow {
            emit(
                Source.Error<List<AuthorPhotos>>(
                    NetworkError("", 500)
                )
            )
        }

        setupViewModel()

        coVerify(exactly = 1) { getAuthorPhotoUseCase(any(), any()) }
        coVerify(exactly = 1) { getAuthorProfileUseCase("username") }
        confirmVerified(getAuthorProfileUseCase)
        confirmVerified(getAuthorPhotoUseCase)

        assertTrue(viewModel.authorPhotosState.value is ViewState.Error)
    }

    @Test
    fun `Should set photos viewstate to error when get author photos succeeds with a null object`() =
        runBlocking {
            coEvery { getAuthorProfileUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        AuthorProfileTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery {
                getAuthorPhotoUseCase(
                    any(),
                    any()
                )
            } returns flow { emit(Source.Success(null)) }

            setupViewModel()

            coVerify(exactly = 1) { getAuthorPhotoUseCase(any(), any()) }
            coVerify(exactly = 1) { getAuthorProfileUseCase("username") }
            confirmVerified(getAuthorProfileUseCase)
            confirmVerified(getAuthorPhotoUseCase)

            assertTrue(viewModel.authorPhotosState.value is ViewState.Error)
        }

    @Test
    fun `Should add photos to list when get more photos succeeds`() = runBlocking {
        coEvery {
            getAuthorPhotoUseCase(
                any(),
                any()
            )
        } returns flow { emit(Source.Success(AuthorPhotosTestData.DOMAIN_RESPONSE)) }
        coEvery { getAuthorProfileUseCase(any()) } returns flow {
            emit(
                Source.Success(
                    AuthorProfileTestData.DOMAIN_RESPONSE
                )
            )
        }
        setupViewModel()

        val expectedPhotos = mutableListOf<AuthorPhotos>()
        expectedPhotos.addAll(AuthorPhotosTestData.DOMAIN_RESPONSE)
        expectedPhotos.addAll(AuthorPhotosTestData.DOMAIN_RESPONSE)

        viewModel.getMoreAuthorPhotos()

        coVerify(exactly = 2) { getAuthorPhotoUseCase(any(), any()) }
        confirmVerified(getAuthorPhotoUseCase)

        assertEquals(
            expectedPhotos,
            (viewModel.authorPhotosState.value as ViewState.Ready).data.toList()
        )
    }

    @Test
    fun `Should show error toast when get more photos fails`() = runBlockingTest {
        coEvery {
            getAuthorPhotoUseCase(
                any(),
                any()
            )
        } returns flow<Source<List<AuthorPhotos>>> {
            emit(
                Source.Error(
                    NetworkError(
                        "",
                        500
                    )
                )
            )
        }

        setupViewModel()

        viewModel.events.test {

            viewModel.getMoreAuthorPhotos()

            coVerify(exactly = 1) { getAuthorPhotoUseCase(any(), any()) }
            confirmVerified(getAuthorPhotoUseCase)


            val emission = awaitItem()
            assertTrue(emission is AuthorDetailsEvents.ShowErrorToast)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should show error toast when get more photos succeeds but returns a null object`() =
        runBlocking {
            coEvery {
                getAuthorPhotoUseCase(
                    any(),
                    any()
                )
            } returns flow { emit(Source.Success(null)) }

            setupViewModel()

            viewModel.events.test {
                viewModel.getMoreAuthorPhotos()

                coVerify(exactly = 1) { getAuthorPhotoUseCase(any(), any()) }
                confirmVerified(getAuthorPhotoUseCase)

                val emission = awaitItem()
                assertTrue(emission is AuthorDetailsEvents.ShowErrorToast)
                cancelAndIgnoreRemainingEvents()
            }
        }
}