package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.testdata.ValidateLoginTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull

class ValidateLoginUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: ValidateLoginUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)
    private val getApiKeyUseCase: GetApiKeyUseCase = mockk(relaxed = true)
    private val getApiSecretKeyUseCase: GetApiSecretKeyUseCase = mockk(relaxed = true)

    override fun setUp() {
        useCase = ValidateLoginUseCase(repository, getApiKeyUseCase, getApiSecretKeyUseCase)
    }

    @Test
    fun `Should validate login in repository layer`() = runBlocking {
        coEvery {
            repository.validateLogin(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Source.Success(ValidateLoginTestData.DOMAIN_RESPONSE)

        val result = useCase.invoke("authCode")

        assertNotNull(result)
        coVerify(exactly = 1) {
            repository.validateLogin(
                authCode = "authCode",
                grantType = any(),
                redirectUri = any(),
                clientSecret = any(),
                clientId = any()
            )
        }
        confirmVerified(repository)
    }
}