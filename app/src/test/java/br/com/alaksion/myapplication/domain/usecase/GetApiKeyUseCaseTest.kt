package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.BuildConfig
import br.com.alaksion.myapplication.ImagefyBaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetApiKeyUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetApiKeyUseCase

    override fun setUp() {
        useCase = GetApiKeyUseCase()
    }

    @Test
    fun `Should get api key from build config`() = runBlocking {
        val result = useCase.invoke()

        assertEquals(BuildConfig.PUBLIC_KEY, result)
    }
}