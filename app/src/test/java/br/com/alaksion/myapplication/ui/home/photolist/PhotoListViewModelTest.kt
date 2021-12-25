package br.com.alaksion.myapplication.ui.home.photolist

import app.cash.turbine.test
import br.com.alaksion.myapplication.ui.model.ViewState
import br.com.alaksion.myapplication.domain.model.Photo
import br.com.alaksion.myapplication.domain.usecase.GetPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.myapplication.testdata.GetImagesTestData
import br.com.alaksion.myapplication.utils.ImagefyBaseViewModelTest
import br.com.alaksion.network.NetworkError
import br.com.alaksion.network.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PhotoListViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: PhotoListViewModel
    private val getPhotosUseCase: GetPhotosUseCase = mockk(relaxed = true)
    private val ratePhotosUseCase: RatePhotoUseCase = mockk(relaxed = true)


    override fun setUp() {
        viewModel = PhotoListViewModel(getPhotosUseCase, ratePhotosUseCase)
    }


    @Test
    fun `Should get images and add to list`() = runBlockingTest {
        coEvery { getPhotosUseCase(any()) } returns flow { emit(Source.Success(GetImagesTestData.DOMAIN_RESPONSE)) }

        coVerify(exactly = 1) { getPhotosUseCase(any()) }
        confirmVerified(getPhotosUseCase)
        assertEquals(GetImagesTestData.DOMAIN_RESPONSE, viewModel.photos.toList())
    }

    @Test
    fun `Should set viewstate to error if get images fails`() = runBlocking {
        coEvery { getPhotosUseCase(any()) } returns flow {
            emit(
                Source.Error<List<Photo>>(
                    NetworkError(
                        "",
                        500
                    )
                )
            )
        }

        coVerify(exactly = 1) { getPhotosUseCase(1) }
        confirmVerified(getPhotosUseCase)
        assertTrue(viewModel.screenState.value is ViewState.Error)
    }

    @Test
    fun `Should set viewstate to error if get images succeeds but returns a null object`() =
        runBlocking {
            coEvery { getPhotosUseCase(any()) } returns flow { emit(Source.Success(null)) }

            coVerify(exactly = 1) { getPhotosUseCase(1) }
            confirmVerified(getPhotosUseCase)
            assertTrue(viewModel.screenState.value is ViewState.Error)
        }

    @Test
    fun `Should get more photos and add them to the photo list`() = runBlocking {
        coEvery { getPhotosUseCase(any()) } returns flow { emit(Source.Success(GetImagesTestData.DOMAIN_RESPONSE)) }

        viewModel.loadMorePhotos()

        coVerify(exactly = 1) { getPhotosUseCase(1) }
        coVerify(exactly = 1) { getPhotosUseCase(2) }
        confirmVerified(getPhotosUseCase)
        assertEquals(viewModel.photos.toList(), GetImagesTestData.DOMAIN_RESPONSE)
    }

    @Test
    fun `Should set show error toast when get more photos fails`() = runBlocking {
        coEvery { getPhotosUseCase(any()) } returns flow {
            emit(
                Source.Error<List<Photo>>(
                    NetworkError(
                        "",
                        500
                    )
                )
            )
        }


        viewModel.events.test {

            viewModel.loadMorePhotos()

            coVerify(exactly = 1) { getPhotosUseCase(1) }
            coVerify(exactly = 1) { getPhotosUseCase(2) }
            confirmVerified(getPhotosUseCase)

            val emission = awaitItem()
            assertTrue(emission is PhotoListEvents.ShowMorePhotosError)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should set show error toast when get more photos succeeds with null response`() =
        runBlocking {
            coEvery { getPhotosUseCase(any()) } returns flow { emit(Source.Success(null)) }

            viewModel.events.test {

                viewModel.loadMorePhotos()

                coVerify(exactly = 1) { getPhotosUseCase(1) }
                coVerify(exactly = 1) { getPhotosUseCase(2) }
                confirmVerified(getPhotosUseCase)

                val emission = awaitItem()
                assertTrue(emission is PhotoListEvents.ShowMorePhotosError)
                cancelAndConsumeRemainingEvents()
            }

        }

    @Test
    fun `Should rate photo`() = runBlocking {
        coEvery { ratePhotosUseCase(any(), any()) } returns flow { emit(Source.Success(Unit)) }

        viewModel.ratePhoto(
            Photo(
                "photoId",
                1,
                "",
                "",
                "",
                "",
                "",
                false,
                ""
            ), false
        )

        coVerify(exactly = 1) { ratePhotosUseCase(false, "photoId") }
        confirmVerified(ratePhotosUseCase)
    }

}