package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.ImagefyBaseTest
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertNotNull

class GetAuthUrlUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetAuthUrlUseCase
    private val getApiKeyUseCase: GetApiKeyUseCase = mockk(relaxed = true)

    override fun setUp() {
        useCase = GetAuthUrlUseCase(getApiKeyUseCase)
    }

    @Test
    fun `Should build api url`() {
        val result = useCase()

        assertNotNull(result)
        verify(exactly = 1) { getApiKeyUseCase() }
        confirmVerified(getApiKeyUseCase)
    }

}