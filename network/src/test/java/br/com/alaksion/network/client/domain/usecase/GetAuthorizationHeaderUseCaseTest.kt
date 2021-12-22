package br.com.alaksion.network.client.domain.usecase

import android.content.SharedPreferences
import br.com.alaksion.network.client.NetworkClientConfig
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAuthorizationHeaderUseCaseTest {

    private lateinit var useCase: GetAuthorizationHeaderUseCase
    private val sharedPrefs: SharedPreferences = mockk()

    @Before
    fun setUp() {
        useCase = GetAuthorizationHeaderUseCase(sharedPrefs)
    }

    @Test
    fun `Should get auth token from shared preferences`() = runBlocking {
        coEvery { sharedPrefs.getString(NetworkClientConfig.AUTH_TOKEN_KEY, "") } returns "Token"

        useCase()

        coVerify(exactly = 1) { sharedPrefs.getString(any(), any()) }
        confirmVerified(sharedPrefs)
    }

}