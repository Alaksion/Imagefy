package br.com.alaksion.myapplication.ui.home.searchphotos

import app.cash.turbine.test
import br.com.alaksion.myapplication.ui.model.ViewState
import br.com.alaksion.myapplication.domain.model.SearchPhotos
import br.com.alaksion.myapplication.domain.usecase.SearchPhotosUseCase
import br.com.alaksion.myapplication.testdata.SearchPhotosTestData
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
class SearchPhotosViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: SearchPhotosViewModel
    private val searchPhotosUseCase: SearchPhotosUseCase = mockk(relaxed = true)

    override fun setUp() {
        viewModel = SearchPhotosViewModel(searchPhotosUseCase)
    }

    @Test
    fun `Should update search query`() {
        val searchQuery = "searchQuery"

        viewModel.onChangeSearchQuery("searchQuery")

        assertEquals(viewModel.searchQuery.value, searchQuery)
    }

    @Test
    fun `Should search for photos according to current search query`() = runBlocking {
        coEvery {
            searchPhotosUseCase(
                any(),
                any()
            )
        } returns flow { emit(Source.Success(SearchPhotosTestData.DOMAIN_RESPONSE)) }
        val searchQuery = "searchQuery"

        viewModel.onChangeSearchQuery(searchQuery)
        viewModel.searchPhotos()

        coVerify(exactly = 1) { searchPhotosUseCase(any(), searchQuery) }
        confirmVerified(searchPhotosUseCase)
        assertEquals(viewModel.photoList.toList(), SearchPhotosTestData.DOMAIN_RESPONSE.photos)
    }

    @Test
    fun `Should should set viewstate to error if search photos succeeds with null response`() =
        runBlocking {
            coEvery {
                searchPhotosUseCase(
                    any(),
                    any()
                )
            } returns flow { emit(Source.Success(null)) }
            val searchQuery = "searchQuery"

            viewModel.onChangeSearchQuery(searchQuery)
            viewModel.searchPhotos()

            coVerify(exactly = 1) { searchPhotosUseCase(any(), searchQuery) }
            confirmVerified(searchPhotosUseCase)
            assertTrue(viewModel.screenState.value is ViewState.Error)
        }

    @Test
    fun `Should should set viewstate to error if search photos fails`() =
        runBlocking {
            coEvery {
                searchPhotosUseCase(
                    any(),
                    any()
                )
            } returns flow { emit(Source.Error<SearchPhotos>(NetworkError("500", 0))) }
            val searchQuery = "searchQuery"

            viewModel.onChangeSearchQuery(searchQuery)
            viewModel.searchPhotos()

            coVerify(exactly = 1) { searchPhotosUseCase(any(), searchQuery) }
            confirmVerified(searchPhotosUseCase)
            assertTrue(viewModel.screenState.value is ViewState.Error)
        }

    @Test
    fun `Should append photos to list when load more photos succeeds`() = runBlocking {
        coEvery {
            searchPhotosUseCase(
                any(),
                any()
            )
        } returns flow { emit(Source.Success(SearchPhotosTestData.DOMAIN_RESPONSE)) }

        viewModel.searchPhotos()
        viewModel.loadMorePhotos()

        coVerify(exactly = 2) { searchPhotosUseCase(any(), any()) }
        confirmVerified(searchPhotosUseCase)
        assertEquals(
            viewModel.photoList.toList().size,
            SearchPhotosTestData.DOMAIN_RESPONSE.photos.size * 2
        )
    }

    @Test
    fun `Should show error toast when load more photos fails`() = runBlocking {
        coEvery {
            searchPhotosUseCase(
                1,
                any()
            )
        } returns flow { emit(Source.Success(SearchPhotosTestData.DOMAIN_RESPONSE)) }

        coEvery {
            searchPhotosUseCase(
                2,
                any()
            )
        } returns flow { emit(Source.Error<SearchPhotos>(NetworkError("", 500))) }

        viewModel.events.test {

            viewModel.searchPhotos()
            viewModel.loadMorePhotos()

            coVerify(exactly = 1) { searchPhotosUseCase(1, any()) }
            coVerify(exactly = 1) { searchPhotosUseCase(2, any()) }
            confirmVerified(searchPhotosUseCase)

            val emission = awaitItem()
            assertTrue(emission is SearchPhotosEvents.MorePhotosError)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should show error toast when load more photos succeeds with null response`() =
        runBlocking {
            coEvery {
                searchPhotosUseCase(
                    1,
                    any()
                )
            } returns flow { emit(Source.Success(SearchPhotosTestData.DOMAIN_RESPONSE)) }

            coEvery {
                searchPhotosUseCase(
                    2,
                    any()
                )
            } returns flow { emit(Source.Success(null)) }

            viewModel.events.test {

                viewModel.searchPhotos()
                viewModel.loadMorePhotos()

                coVerify(exactly = 1) { searchPhotosUseCase(1, "") }
                coVerify(exactly = 1) { searchPhotosUseCase(2, "") }
                confirmVerified(searchPhotosUseCase)

                val emission = awaitItem()
                assertTrue(emission is SearchPhotosEvents.MorePhotosError)
                cancelAndIgnoreRemainingEvents()
            }

        }

}