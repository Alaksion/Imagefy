package br.com.alaksion.network.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alaksion.network.Source
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class HandleApiResponseKtTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Should map response when request succeeds with body`() {
        val result = HandleApiResponseData.SuccessResponseContent.handleApiResponse()

        assertTrue(result is Source.Success)
        assertTrue(result.data == "success")
    }

}