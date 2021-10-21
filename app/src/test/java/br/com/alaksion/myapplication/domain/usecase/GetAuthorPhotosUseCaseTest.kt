package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.ImagefyBaseTest
import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.testdata.AuthorPhotosTestData
import br.com.alaksion.myapplication.testdata.UserNameTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAuthorPhotosUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetAuthorPhotosUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = GetAuthorPhotosUseCase(repository)
    }

    @Test
    fun `Should get author photos from repository layer`() = runBlocking {
        coEvery {
            repository.getAuthorPhotos(
                any(),
                any()
            )
        } returns Source.Success(AuthorPhotosTestData.DOMAIN_RESPONSE)

        val result = useCase.invoke("authorId", 1)

        kotlin.test.assertNotNull(result)
        coVerify(exactly = 1) { repository.getAuthorPhotos("authorId", 1) }
        confirmVerified(repository)
    }

}