package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.model.SearchPhotosRequest
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.testdata.SearchPhotosTestData
import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import br.com.alaksion.network.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull

class SearchPhotosUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: SearchPhotosUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = SearchPhotosUseCase(repository)
    }

    @Test
    fun `Should search photos from layer`() = runBlocking {
        coEvery {
            repository.searchPhotos(any())
        } returns flow { emit(Source.Success(SearchPhotosTestData.DOMAIN_RESPONSE)) }

        val result = useCase.invoke(1, "photoId")

        assertNotNull(result)
        coVerify(exactly = 1) { repository.searchPhotos(SearchPhotosRequest(1, "photoId")) }
        confirmVerified(repository)
    }

}