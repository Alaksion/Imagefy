package br.com.alaksion.myapplication.domain.repository

import br.com.alaksion.myapplication.domain.model.*
import br.com.alaksion.network.Source
import kotlinx.coroutines.flow.Flow

interface ImagefyRepository {

    suspend fun validateLogin(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        authCode: String,
        grantType: String
    ): Flow<Source<Auth>>

    suspend fun getPhotos(page: Int): Flow<Source<List<Photo>>>

    suspend fun getAuthorProfile(username: String): Flow<Source<Author>>

    suspend fun getAuthorPhotos(
        username: String,
        page: Int
    ): Flow<Source<List<AuthorPhotos>>>

    suspend fun getPhotoDetails(photoId: String): Flow<Source<PhotoDetail>>

    suspend fun likePhoto(photoId: String): Flow<Source<Unit>>

    suspend fun unlikePhoto(photoId: String): Flow<Source<Unit>>

    suspend fun getCurrentUsername(): Flow<Source<CurrentUser>>

    suspend fun searchPhotos(request: SearchPhotosRequest): Flow<Source<SearchPhotos>>

    suspend fun storeDarkModeConfig(value: Boolean)

    fun getCurrentDarkModeConfig(): Flow<Boolean>

    suspend fun storeCurrentUser(user: StoredUser)

    suspend fun getCurrentUser(): Flow<StoredUser>


}