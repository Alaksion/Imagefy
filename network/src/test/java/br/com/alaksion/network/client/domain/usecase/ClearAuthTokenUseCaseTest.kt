package br.com.alaksion.network.client.domain.usecase

import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ClearAuthTokenUseCaseTest {

    private lateinit var useCase: ClearAuthTokenUseCase
    private val repository: NetworkClientRepository = mockk()

    @Before
    fun setUp() {
        useCase = ClearAuthTokenUseCase(repository)
    }

    @Test
    fun `Should clear auth token in repository layer`() = runBlocking {
        coEvery { repository.clearAuthorizationHeader() } returns Unit

        useCase()

        coVerify(exactly = 1) { repository.clearAuthorizationHeader() }
        confirmVerified(repository)
    }

}