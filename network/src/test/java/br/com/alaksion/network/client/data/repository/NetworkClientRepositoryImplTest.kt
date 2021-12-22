package br.com.alaksion.network.client.data.repository

import br.com.alaksion.network.client.data.datasource.NetworkClientLocalDataSource
import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class NetworkClientRepositoryImplTest {

    private lateinit var repository: NetworkClientRepository
    private val localDataSource: NetworkClientLocalDataSource = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = NetworkClientRepositoryImpl(localDataSource)
    }

    @Test
    fun `Should clear authorization header in data source layer`() {
        every { localDataSource.clearAuthorizationHeader() } returns Unit

        repository.clearAuthorizationHeader()

        verify(exactly = 1) { localDataSource.clearAuthorizationHeader() }
        confirmVerified(localDataSource)
    }

    @Test
    fun `Should store authorization header in data source layer`() {
        every { localDataSource.storeAuthorizationHeader("header") } returns Unit

        repository.storeAuthorizationHeader("header")

        verify(exactly = 1) { localDataSource.storeAuthorizationHeader("header") }
        confirmVerified(localDataSource)
    }


}