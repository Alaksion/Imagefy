package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.BuildConfig
import br.com.alaksion.myapplication.ImagefyBaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetApiSecretKeyUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetApiSecretKeyUseCase

    override fun setUp() {
        useCase = GetApiSecretKeyUseCase()
    }

    @Test
    fun `Should get api secret key from build config`() = runBlocking {
        val result = useCase.invoke()

        assertEquals(BuildConfig.SECRET_KEY, result)
    }
}