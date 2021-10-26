package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.testdata.PhotoDetailsTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetPhotoDetailsUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetPhotoDetailsUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = GetPhotoDetailsUseCase(repository)
    }

    @Test
    fun `Should get photo details from repository layer`() = runBlocking {
        coEvery { repository.getPhotoDetails(any()) } returns Source.Success(PhotoDetailsTestData.DOMAIN_RESPONSE)

        val result = useCase.invoke("photoId")

        kotlin.test.assertNotNull(result)
        coVerify(exactly = 1) { repository.getPhotoDetails("photoId") }
        confirmVerified(repository)
    }

}