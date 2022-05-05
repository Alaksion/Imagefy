package br.com.alaksion.network.client.domain.usecase

import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAuthorizationHeaderUseCaseTest {

    private lateinit var useCase: GetAuthorizationHeaderUseCase
    private val sharedPrefs: NetworkClientRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetAuthorizationHeaderUseCase(sharedPrefs)
    }

    @Test
    fun `Should get auth token from shared preferences`() = runBlocking {
        coEvery { sharedPrefs.getAuthorizationHeader() } returns "Token"

        useCase()

        coVerify(exactly = 1) { sharedPrefs.getAuthorizationHeader() }
        confirmVerified(sharedPrefs)
    }

}