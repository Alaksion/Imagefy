package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.ImagefyBaseTest
import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.testdata.GetImagesTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

class GetPhotosUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetPhotosUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = GetPhotosUseCase(repository)
    }

    @Test
    fun `Should get photos from repository layer`() = runBlocking {
        coEvery { repository.getPhotos(any()) } returns Source.Success(GetImagesTestData.DOMAIN_RESPONSE)

        val result = useCase.invoke(1)

        assertNotNull(result)
        coVerify(exactly = 1) { repository.getPhotos(1) }
        confirmVerified(repository)
    }

}