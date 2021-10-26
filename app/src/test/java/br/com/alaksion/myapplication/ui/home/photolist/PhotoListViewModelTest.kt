package br.com.alaksion.myapplication.ui.home.photolist

import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.usecase.GetPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.myapplication.testdata.GetImagesTestData
import br.com.alaksion.myapplication.utils.ImagefyBaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PhotoListViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: PhotoListViewModel
    private val getPhotosUseCase: GetPhotosUseCase = mockk(relaxed = true)
    private val ratePhotosUseCase: RatePhotoUseCase = mockk(relaxed = true)


    override fun setUp() {
        viewModel = PhotoListViewModel(getPhotosUseCase, ratePhotosUseCase)
    }


    @Test
    fun `Should get images and add to list`() = runBlocking {
        coEvery { getPhotosUseCase(any()) } returns Source.Success(GetImagesTestData.DOMAIN_RESPONSE)

        viewModel.getImages()

        coVerify(exactly = 1) { getPhotosUseCase(any()) }
        confirmVerified(getPhotosUseCase)
        assertEquals(viewModel.photos.toList(), GetImagesTestData.DOMAIN_RESPONSE)
    }

    @Test
    fun `Should set viewstate to error if get images fails`() = runBlocking {
        coEvery { getPhotosUseCase(any()) } returns Source.Error(NetworkError("", 500))

        viewModel.getImages()

        coVerify(exactly = 1) { getPhotosUseCase(any()) }
        confirmVerified(getPhotosUseCase)
        assertTrue(viewModel.screenState.value is ViewState.Error)
    }

    @Test
    fun `Should set viewstate to error if get images succeeds but returns a null object`() =
        runBlocking {
            coEvery { getPhotosUseCase(any()) } returns Source.Success(null)

            viewModel.getImages()

            coVerify(exactly = 1) { getPhotosUseCase(any()) }
            confirmVerified(getPhotosUseCase)
            assertTrue(viewModel.screenState.value is ViewState.Error)
        }

    @Test
    fun `Should get more photos and add them to the photo list`() = runBlocking {
        coEvery { getPhotosUseCase(any()) } returns Source.Success(GetImagesTestData.DOMAIN_RESPONSE)

        viewModel.loadMorePhotos()

        coVerify(exactly = 1) { getPhotosUseCase(any()) }
        confirmVerified(getPhotosUseCase)
        assertEquals(viewModel.photos.toList(), GetImagesTestData.DOMAIN_RESPONSE)
    }

    @Test
    fun `Should set show error toast when get more photos fails`() = runBlocking {
        coEvery { getPhotosUseCase(any()) } returns Source.Error(NetworkError("", 500))

        viewModel.loadMorePhotos()

        coVerify(exactly = 1) { getPhotosUseCase(any()) }
        confirmVerified(getPhotosUseCase)
        assertNotNull(viewModel.showMorePhotosError.value?.getContentIfNotHandled())
    }

    @Test
    fun `Should set show error toast when get more photos succeeds with null response`() =
        runBlocking {
            coEvery { getPhotosUseCase(any()) } returns Source.Success(null)

            viewModel.loadMorePhotos()

            coVerify(exactly = 1) { getPhotosUseCase(any()) }
            confirmVerified(getPhotosUseCase)
            assertNotNull(viewModel.showMorePhotosError.value?.getContentIfNotHandled())
        }

    @Test
    fun `Should rate photo`() = runBlocking {
        coEvery { ratePhotosUseCase(any(), any()) } returns Source.Success(Unit)

        viewModel.ratePhoto("photoId", false)

        coVerify(exactly = 1) { ratePhotosUseCase(false, "photoId") }
        confirmVerified(ratePhotosUseCase)
    }

}