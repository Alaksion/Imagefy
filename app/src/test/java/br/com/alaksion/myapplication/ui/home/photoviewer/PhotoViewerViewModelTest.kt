package br.com.alaksion.myapplication.ui.home.photoviewer

import br.com.alaksion.myapplication.domain.model.PhotoDetail
import br.com.alaksion.myapplication.domain.usecase.GetPhotoDetailsUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.myapplication.testdata.PhotoDetailsTestData
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
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class PhotoViewerViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: PhotoViewerViewModel
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase = mockk(relaxed = true)
    private val ratePhotoUseCase: RatePhotoUseCase = mockk(relaxed = true)

    private fun setUpViewModel() {
        viewModel = PhotoViewerViewModel("photoId", getPhotoDetailsUseCase, ratePhotoUseCase)
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

        setUpViewModel()

        coVerify(exactly = 1) { getPhotoDetailsUseCase("photoId") }
        confirmVerified(getPhotoDetailsUseCase)
        assertEquals(
            (viewModel.photoData.value as PhotoViewerState.Ready).photoData,
            PhotoDetailsTestData.DOMAIN_RESPONSE
        )
    }

    @Test
    fun `Should set viewstate to error if get photo details fails`() = runBlocking {
        coEvery { getPhotoDetailsUseCase(any()) } returns flow {
            emit(
                Source.Error<PhotoDetail>(
                    NetworkError(
                        "",
                        500
                    )
                )
            )
        }

        setUpViewModel()

        coVerify(exactly = 1) { getPhotoDetailsUseCase("photoId") }
        confirmVerified(getPhotoDetailsUseCase)
        assertTrue(viewModel.photoData.value is PhotoViewerState.Error)
    }

    @Test
    fun `Should set viewstate to error if get photo details succeeds but returns a null object`() =
        runBlocking {
            coEvery { getPhotoDetailsUseCase(any()) } returns flow { emit(Source.Success(null)) }

            setUpViewModel()

            coVerify(exactly = 1) { getPhotoDetailsUseCase("photoId") }
            confirmVerified(getPhotoDetailsUseCase)
            assertTrue(viewModel.photoData.value is PhotoViewerState.Error)
        }

    @Test
    fun `Should rate photo`() = runBlocking {
        coEvery { ratePhotoUseCase(any(), any()) } returns flow { emit(Source.Success(Unit)) }

        setUpViewModel()

        viewModel.ratePhoto("photoId", false)

        coVerify(exactly = 1) { ratePhotoUseCase(false, "photoId") }
        confirmVerified(ratePhotoUseCase)
    }

}