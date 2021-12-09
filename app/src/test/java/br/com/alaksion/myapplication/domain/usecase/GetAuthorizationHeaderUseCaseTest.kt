package br.com.alaksion.myapplication.domain.usecase

import android.content.SharedPreferences
import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import br.com.alaksion.myapplication.data.local.AppLocalConfig
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
    private val sharedPrefs: SharedPreferences = mockk(relaxed = true)

    override fun setUp() {
        useCase = GetAuthorizationHeaderUseCase(sharedPrefs)
    }

    @Test
    fun `Should get authorization header from shared preferences`() = runBlocking {
        every {
            sharedPrefs.getString(any(), any())
        } returns "Header"

        val result = useCase.invoke()

        assertNotNull(result)
        coVerify(exactly = 1) { sharedPrefs.getString(AppLocalConfig.AUTH_TOKEN_KEY, "") }
        confirmVerified(sharedPrefs)
    }
}
