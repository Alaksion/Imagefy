package br.com.alaksion.network.client.data.repository

import br.com.alaksion.network.client.data.datasource.NetworkClientLocalDataSource
import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import javax.inject.Inject

class NetworkClientRepositoryImpl @Inject constructor(
    private val localDataSource: NetworkClientLocalDataSource
) : NetworkClientRepository {

    override fun storeAuthorizationHeader(value: String) {
        localDataSource.storeAuthorizationHeader(value)
    }

    override fun clearAuthorizationHeader() {
        localDataSource.clearAuthorizationHeader()
    }

    override fun getAuthorizationHeader(): String {
        return localDataSource.getAuthorizationHeader()
    }

}