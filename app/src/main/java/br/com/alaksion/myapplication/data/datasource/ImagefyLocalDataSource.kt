package br.com.alaksion.myapplication.data.datasource

import kotlinx.coroutines.flow.Flow

interface ImagefyLocalDataSource {

    fun storeAuthorizationHeader(value: String)

    fun clearAuthorizationHeader()

    suspend fun storeDarkModeConfig(value: Boolean)

    fun getCurrentDarkModeConfig(): Flow<Boolean>

}