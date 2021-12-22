package br.com.alaksion.myapplication.ui.home.userprofile

import br.com.alaksion.network.NetworkError
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.usecase.GetAuthorPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.testdata.AuthorPhotosTestData
import br.com.alaksion.myapplication.testdata.AuthorProfileTestData
import br.com.alaksion.myapplication.utils.ImagefyBaseViewModelTest
import br.com.alaksion.network.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class UserProfileViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: UserProfileViewModel
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase = mockk(relaxed = true)
    private val getAuthorPhotoUseCase: GetAuthorPhotosUseCase = mockk(relaxed = true)

    override fun setUp() {
        viewModel = UserProfileViewModel(getAuthorProfileUseCase, getAuthorPhotoUseCase)
    }

    @Test
    fun `Should call get author profile and set viewState`() = runBlocking {
        coEvery { getAuthorProfileUseCase(any()) } returns flow {
            emit(
                Source.Success(
                    AuthorProfileTestData.DOMAIN_RESPONSE
                )
            )
        }

        viewModel.getUserProfileData("username")

        coVerify(exactly = 1) { getAuthorProfileUseCase("username") }
        confirmVerified(getAuthorProfileUseCase)
        assertEquals(
            (viewModel.userData.value as ViewState.Ready).data,
            AuthorProfileTestData.DOMAIN_RESPONSE
        )
    }

    @Test
    fun `Should set viewstate to error if get author profile succeeds with null response`() =
        runBlocking {
            coEvery { getAuthorProfileUseCase(any()) } returns flow { emit(Source.Success(null)) }

            viewModel.getUserProfileData("username")

            coVerify(exactly = 1) { getAuthorProfileUseCase("username") }
            confirmVerified(getAuthorProfileUseCase)
            assertTrue(viewModel.userData.value is ViewState.Error)
        }

    @Test
    fun `Should set viewstate to error if get author profile fails`() =
        runBlocking {
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

            viewModel.getUserProfileData("username")

            coVerify(exactly = 1) { getAuthorProfileUseCase("username") }
            confirmVerified(getAuthorProfileUseCase)
            assertTrue(viewModel.userData.value is ViewState.Error)
        }

    @Test
    fun `Should call get author photos after get author profile success and set viewState`() =
        runBlocking {
            coEvery { getAuthorProfileUseCase(any()) } returns flow {
                emit(
                    Source.Success(
                        AuthorProfileTestData.DOMAIN_RESPONSE
                    )
                )
            }
            coEvery { getAuthorPhotoUseCase(any(), any()) } returns flow {
                emit(
                    Source.Success(
                        AuthorPhotosTestData.DOMAIN_RESPONSE
                    )
                )
            }

            viewModel.getUserProfileData("username")

            coVerify(exactly = 1) { getAuthorPhotoUseCase("username", 1) }
            confirmVerified(getAuthorPhotoUseCase)
            assertEquals(viewModel.userPhotos.toList(), AuthorPhotosTestData.DOMAIN_RESPONSE)
        }

    @Test
    fun `Should set photos state to error when get user photos fails`() =
        runBlocking {
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
                        NetworkError(
                            "",
                            500
                        )
                    )
                )
            }

            viewModel.getUserProfileData("username")

            coVerify(exactly = 1) { getAuthorPhotoUseCase("username", 1) }
            confirmVerified(getAuthorPhotoUseCase)
            assertTrue(viewModel.userPhotosState.value is ViewState.Error)
        }

    @Test
    fun `Should set photos state to error when get user photos succeeds with null response`() =
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

            viewModel.getUserProfileData("username")

            coVerify(exactly = 1) { getAuthorPhotoUseCase("username", 1) }
            confirmVerified(getAuthorPhotoUseCase)
            assertTrue(viewModel.userPhotosState.value is ViewState.Error)
        }

    @Test
    fun `Should add more photos to list when get more photos succeeds`() =
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

            viewModel.getUserProfileData("username")

            coVerify(exactly = 1) { getAuthorPhotoUseCase("username", 1) }
            confirmVerified(getAuthorPhotoUseCase)
            assertTrue(viewModel.userPhotosState.value is ViewState.Error)
        }


}