package br.com.alaksion.myapplication.ui.home.authordetails

import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.usecase.GetAuthorPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.testdata.AuthorPhotosTestData
import br.com.alaksion.myapplication.testdata.AuthorProfileTestData
import br.com.alaksion.myapplication.utils.ImagefyBaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class AuthorDetailsViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: AuthorDetailsViewModel
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase = mockk(relaxed = true)
    private val getAuthorPhotoUseCase: GetAuthorPhotosUseCase = mockk(relaxed = true)

    override fun setUp() {
        viewModel = AuthorDetailsViewModel(getAuthorProfileUseCase, getAuthorPhotoUseCase)
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

        viewModel.getAuthorProfileData(AuthorProfileTestData.DOMAIN_RESPONSE.username)

        coVerify(exactly = 1) {
            getAuthorPhotoUseCase(
                AuthorProfileTestData.DOMAIN_RESPONSE.username,
                1
            )
        }
        coVerify(exactly = 1) { getAuthorProfileUseCase(AuthorProfileTestData.DOMAIN_RESPONSE.username) }
        confirmVerified(getAuthorProfileUseCase)
        confirmVerified(getAuthorPhotoUseCase)

        assertEquals(viewModel.authorPhotos.toList(), AuthorPhotosTestData.DOMAIN_RESPONSE)
        assertEquals(
            (viewModel.authorData.value as ViewState.Ready).data,
            AuthorProfileTestData.DOMAIN_RESPONSE
        )
    }

    @Test
    fun `Should set viewState to error if get author profile fails`() = runBlocking {
        coEvery { getAuthorProfileUseCase(any()) } returns flow {
            emit(
                Source.Error<AuthorResponse>(
                    NetworkError(
                        "",
                        500
                    )
                )
            )
        }

        viewModel.getAuthorProfileData(AuthorProfileTestData.DOMAIN_RESPONSE.username)

        coVerify(exactly = 0) { getAuthorPhotoUseCase(any(), any()) }
        coVerify(exactly = 1) { getAuthorProfileUseCase(AuthorProfileTestData.DOMAIN_RESPONSE.username) }
        confirmVerified(getAuthorProfileUseCase)
        confirmVerified(getAuthorPhotoUseCase)

        assertTrue(viewModel.authorData.value is ViewState.Error)
    }

    @Test
    fun `Should set viewState to error if get author profile succeeds but returns a null object`() =
        runBlocking {
            coEvery { getAuthorProfileUseCase(any()) } returns flow { emit(Source.Success(null)) }

            viewModel.getAuthorProfileData(AuthorProfileTestData.DOMAIN_RESPONSE.username)

            coVerify(exactly = 0) { getAuthorPhotoUseCase(any(), any()) }
            coVerify(exactly = 1) { getAuthorProfileUseCase(AuthorProfileTestData.DOMAIN_RESPONSE.username) }
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
                Source.Error<List<AuthorPhotosResponse>>(
                    NetworkError("", 500)
                )
            )
        }

        viewModel.getAuthorProfileData(AuthorProfileTestData.DOMAIN_RESPONSE.username)

        coVerify(exactly = 1) { getAuthorPhotoUseCase(any(), any()) }
        coVerify(exactly = 1) { getAuthorProfileUseCase(AuthorProfileTestData.DOMAIN_RESPONSE.username) }
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

            viewModel.getAuthorProfileData(AuthorProfileTestData.DOMAIN_RESPONSE.username)

            coVerify(exactly = 1) { getAuthorPhotoUseCase(any(), any()) }
            coVerify(exactly = 1) { getAuthorProfileUseCase(AuthorProfileTestData.DOMAIN_RESPONSE.username) }
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

        viewModel.getMoreAuthorPhotos()

        coVerify(exactly = 1) { getAuthorPhotoUseCase(any(), any()) }
        confirmVerified(getAuthorPhotoUseCase)

        assertEquals(viewModel.authorPhotos.toList(), AuthorPhotosTestData.DOMAIN_RESPONSE)
    }

    @Test
    fun `Should show error toast when get more photos fails`() = runBlocking {
        coEvery {
            getAuthorPhotoUseCase(
                any(),
                any()
            )
        } returns flow { emit(Source.Error<List<AuthorPhotosResponse>>(NetworkError("", 500))) }

        viewModel.getMoreAuthorPhotos()

        coVerify(exactly = 1) { getAuthorPhotoUseCase(any(), any()) }
        confirmVerified(getAuthorPhotoUseCase)

        assertNotNull(viewModel.showErrorToast.value?.getContentIfNotHandled())
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

            viewModel.getMoreAuthorPhotos()

            coVerify(exactly = 1) { getAuthorPhotoUseCase(any(), any()) }
            confirmVerified(getAuthorPhotoUseCase)

            assertNotNull(viewModel.showErrorToast.value?.getContentIfNotHandled())
        }
}