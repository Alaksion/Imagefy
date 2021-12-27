package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import br.com.alaksion.network.client.domain.usecase.GetAuthorizationHeaderUseCase
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull

class GetAuthorizationHeaderUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetAuthorizationHeaderUseCase
    private val repository: NetworkClientRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = GetAuthorizationHeaderUseCase(repository)
    }

    @Test
    fun `Should get authorization header from shared preferences`() = runBlocking {
        every {
            repository.getAuthorizationHeader()
        } returns "Header"

        val result = useCase.invoke()

        assertNotNull(result)
        coVerify(exactly = 1) { repository.getAuthorizationHeader() }
        confirmVerified(repository)
    }
}
