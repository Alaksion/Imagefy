package br.com.alaksion.myapplication.ui.home.photoviewer

import br.com.alaksion.network.NetworkError
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.domain.usecase.GetPhotoDetailsUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.myapplication.testdata.PhotoDetailsTestData
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
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class PhotoViewerViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: PhotoViewerViewModel
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase = mockk(relaxed = true)
    private val ratePhotoUseCase: RatePhotoUseCase = mockk(relaxed = true)

    override fun setUp() {
        viewModel = PhotoViewerViewModel(getPhotoDetailsUseCase, ratePhotoUseCase)
    }

    @Test
    fun `Should call get photo details use case`() = runBlocking {
        coEvery { getPhotoDetailsUseCase(any()) } returns flow {
            emit(
                Source.Success(
                    PhotoDetailsTestData.DOMAIN_RESPONSE
                )
            )
        }

        viewModel.getPhotoDetails("id")

        coVerify(exactly = 1) { getPhotoDetailsUseCase("id") }
        confirmVerified(getPhotoDetailsUseCase)
        assertEquals(
            (viewModel.photoData.value as ViewState.Ready).data,
            PhotoDetailsTestData.DOMAIN_RESPONSE
        )
    }

    @Test
    fun `Should set viewstate to error if get photo details fails`() = runBlocking {
        coEvery { getPhotoDetailsUseCase(any()) } returns flow {
            emit(
                Source.Error<PhotoDetailResponse>(
                    NetworkError(
                        "",
                        500
                    )
                )
            )
        }

        viewModel.getPhotoDetails("id")

        coVerify(exactly = 1) { getPhotoDetailsUseCase("id") }
        confirmVerified(getPhotoDetailsUseCase)
        assertTrue(viewModel.photoData.value is ViewState.Error)
    }

    @Test
    fun `Should set viewstate to error if get photo details succeeds but returns a null object`() =
        runBlocking {
            coEvery { getPhotoDetailsUseCase(any()) } returns flow { emit(Source.Success(null)) }

            viewModel.getPhotoDetails("id")

            coVerify(exactly = 1) { getPhotoDetailsUseCase("id") }
            confirmVerified(getPhotoDetailsUseCase)
            assertTrue(viewModel.photoData.value is ViewState.Error)
        }

    @Test
    fun `Should rate photo`() = runBlocking {
        coEvery { ratePhotoUseCase(any(), any()) } returns flow { emit(Source.Success(Unit)) }

        viewModel.ratePhoto("photoId", false)

        coVerify(exactly = 1) { ratePhotoUseCase(false, "photoId") }
        confirmVerified(ratePhotoUseCase)
    }

}