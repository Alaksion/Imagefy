package br.com.alaksion.network.client.data.datasource

interface NetworkClientLocalDataSource {

    fun storeAuthorizationHeader(value: String)

    fun clearAuthorizationHeader()

    fun getAuthorizationHeader(): String

}