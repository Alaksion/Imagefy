package br.com.alaksion.network.client.domain.repository

interface NetworkClientRepository {

    fun storeAuthorizationHeader(value: String)

    fun clearAuthorizationHeader()

    fun getAuthorizationHeader(): String

}