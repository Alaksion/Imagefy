package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.ImagefyBaseTest
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertNotNull

class StoreAuthTokenUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: StoreAuthTokenUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = StoreAuthTokenUseCase(repository)
    }

    @Test
    fun `Should store auth token in repository layer`() {
        every { repository.storeAuthorizationHeader(any()) } returns Unit

        val result = useCase.invoke("token")

        assertNotNull(result)
        verify(exactly = 1) { repository.storeAuthorizationHeader("token") }
        confirmVerified(repository)
    }

}