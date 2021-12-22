package br.com.alaksion.network.client.data.local

import android.content.SharedPreferences
import br.com.alaksion.network.client.NetworkClientConfig
import br.com.alaksion.network.client.data.datasource.NetworkClientLocalDataSource
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class NetworkClientLocalDataSourceImplTest {

    private lateinit var localDataSOurce: NetworkClientLocalDataSource
    private val sharedPrefs: SharedPreferences = mockk(relaxed = true)

    @Before
    fun setUp() {
        localDataSOurce = NetworkClientLocalDataSourceImpl(sharedPrefs)
    }

    @Test
    fun `Should clear authorization header from sharedPrefs`() {
        coEvery { sharedPrefs.edit().remove(NetworkClientConfig.AUTH_TOKEN_KEY).apply() } returns Unit

        localDataSOurce.clearAuthorizationHeader()

        verify(exactly = 1) { sharedPrefs.edit().remove(NetworkClientConfig.AUTH_TOKEN_KEY).apply() }
        confirmVerified(sharedPrefs)
    }

    @Test
    fun `Should store authorization header in sharedPrefs`() {
        coEvery {
            sharedPrefs.edit().putString(NetworkClientConfig.AUTH_TOKEN_KEY, "value").apply()
        } returns Unit

        localDataSOurce.storeAuthorizationHeader("value")

        verify(exactly = 1) {
            sharedPrefs.edit().putString(NetworkClientConfig.AUTH_TOKEN_KEY, "value").apply()
        }
        confirmVerified(sharedPrefs)
    }

}