package br.com.alaksion.myapplication.data.datasource

import br.com.alaksion.myapplication.data.model.storeduser.StoredUserData
import kotlinx.coroutines.flow.Flow

interface ImagefyLocalDataSource {

    suspend fun storeDarkModeConfig(value: Boolean)

    fun getCurrentDarkModeConfig(): Flow<Boolean>

    suspend fun storeCurrentUser(user: StoredUserData)

    suspend fun getCurrentUser(): Flow<StoredUserData>

}