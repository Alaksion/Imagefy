package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.testdata.AuthorProfileTestData
import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import br.com.alaksion.network.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAuthorProfileUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetAuthorProfileUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = GetAuthorProfileUseCase(repository)
    }

    @Test
    fun `Should get author profile from repository layer`() = runBlocking {
        coEvery {
            repository.getAuthorProfile(any())
        } returns flow { emit(Source.Success(AuthorProfileTestData.DOMAIN_RESPONSE)) }

        val result = useCase.invoke("authorId")

        kotlin.test.assertNotNull(result)
        coVerify(exactly = 1) { repository.getAuthorProfile("authorId") }
        confirmVerified(repository)
    }

}