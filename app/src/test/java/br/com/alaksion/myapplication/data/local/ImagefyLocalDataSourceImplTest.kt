package br.com.alaksion.myapplication.data.local

import android.content.SharedPreferences
import br.com.alaksion.myapplication.ImagefyBaseTest
import br.com.alaksion.myapplication.data.datasource.ImagefyLocalDataSource
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class ImagefyLocalDataSourceImplTest : ImagefyBaseTest() {

    private lateinit var localDataSource: ImagefyLocalDataSource
    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)

    override fun setUp() {
        localDataSource = ImagefyLocalDataSourceImpl(sharedPreferences)
    }

    @Test
    fun `Should store authorization header`() {
        localDataSource.storeAuthorizationHeader("header")

        verify(exactly = 1) {
            sharedPreferences.edit().putString(AppLocalConfig.AUTH_TOKEN_KEY, "header").apply()
        }
        confirmVerified(sharedPreferences)
    }

    @Test
    fun `Should clear authorization header`() {
        localDataSource.clearAuthorizationHeader()

        verify(exactly = 1) {
            sharedPreferences.edit().remove(AppLocalConfig.AUTH_TOKEN_KEY).apply()
        }
        confirmVerified(sharedPreferences)
    }
}