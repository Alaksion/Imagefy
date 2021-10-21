package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.ImagefyBaseTest
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertNotNull

class GetClientIdUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetClientIdUseCase
    private val getApiKeyUseCase: GetApiKeyUseCase = mockk(relaxed = true)

    override fun setUp() {
        useCase = GetClientIdUseCase(getApiKeyUseCase)
    }

    @Test
    fun `Should build client id`() {
        val result = useCase()

        assertNotNull(result)
        verify(exactly = 1) { getApiKeyUseCase() }
        confirmVerified(getApiKeyUseCase)
    }

}