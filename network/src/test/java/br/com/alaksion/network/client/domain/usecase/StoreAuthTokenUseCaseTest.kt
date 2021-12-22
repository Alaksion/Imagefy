package br.com.alaksion.network.client.domain.usecase

import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class StoreAuthTokenUseCaseTest {

    private lateinit var useCase: StoreAuthTokenUseCase
    private val repository: NetworkClientRepository = mockk()

    @Before
    fun setUp() {
        useCase = StoreAuthTokenUseCase(repository)
    }

    @Test
    fun `Should store auth token in repository layer`() = runBlocking {
        coEvery { repository.storeAuthorizationHeader("token") } returns Unit

        useCase("token")

        coVerify(exactly = 1) { repository.storeAuthorizationHeader("token") }
        confirmVerified(repository)
    }

}