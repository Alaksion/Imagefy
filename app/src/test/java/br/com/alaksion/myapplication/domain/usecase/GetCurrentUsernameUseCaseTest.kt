package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.testdata.UserNameTestData
import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull

class GetCurrentUsernameUseCaseTest : ImagefyBaseTest() {

    private lateinit var useCase: GetCurrentUsernameUseCase
    private val repository: ImagefyRepository = mockk(relaxed = true)

    override fun setUp() {
        useCase = GetCurrentUsernameUseCase(repository)
    }

    @Test
    fun `Should get current username from repository layer`() = runBlocking {
        coEvery { repository.getCurrentUsername() } returns flow {
            emit(
                Source.Success(
                    UserNameTestData.DOMAIN_RESPONSE
                )
            )
        }

        val result = useCase.invoke()

        assertNotNull(result)
        coVerify(exactly = 1) { repository.getCurrentUsername() }
        confirmVerified(repository)
    }

}