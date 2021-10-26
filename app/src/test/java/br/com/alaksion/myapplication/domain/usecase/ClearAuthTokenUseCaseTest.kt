package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull

class ClearAuthTokenUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: ClearAuthTokenUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = ClearAuthTokenUseCase(repository)
    }

    @Test
    fun `Should should clear auth token in repository layer`() = runBlocking {
        coEvery {
            repository.clearAuthorizationHeader()
        } returns Unit

        val result = useCase.invoke()

        assertNotNull(result)
        coVerify(exactly = 1) { repository.clearAuthorizationHeader() }
        confirmVerified(repository)
    }
}