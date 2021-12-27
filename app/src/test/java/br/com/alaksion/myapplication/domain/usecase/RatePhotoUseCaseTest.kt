package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import br.com.alaksion.network.model.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull

class RatePhotoUseCaseTest : ImagefyBaseTest() {
    private lateinit var useCase: RatePhotoUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = RatePhotoUseCase(repository)
    }

    @Test
    fun `Should like photo in repository layer`() = runBlocking {
        coEvery {
            repository.likePhoto(any())
        } returns flow { emit(Source.Success(Unit)) }

        val result = useCase.invoke(true, "photoId")

        assertNotNull(result)
        coVerify(exactly = 1) { repository.likePhoto("photoId") }
        confirmVerified(repository)
    }

    @Test
    fun `Should unlike photo in repository layer`() = runBlocking {
        coEvery {
            repository.likePhoto(any())
        } returns flow { emit(Source.Success(Unit)) }

        val result = useCase.invoke(false, "photoId")

        assertNotNull(result)
        coVerify(exactly = 1) { repository.unlikePhoto("photoId") }
        confirmVerified(repository)
    }
}