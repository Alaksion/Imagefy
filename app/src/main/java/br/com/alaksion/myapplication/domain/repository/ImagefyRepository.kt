package br.com.alaksion.myapplication.domain.repository

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.*
import kotlinx.coroutines.flow.Flow

interface ImagefyRepository {

    suspend fun validateLogin(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        authCode: String,
        grantType: String
    ): Flow<Source<AuthResponse>>

    suspend fun getPhotos(page: Int): Flow<Source<List<PhotoResponse>>>

    suspend fun getAuthorProfile(username: String): Flow<Source<AuthorResponse>>

    suspend fun getAuthorPhotos(
        username: String,
        page: Int
    ): Flow<Source<List<AuthorPhotosResponse>>>

    suspend fun getPhotoDetails(photoId: String): Flow<Source<PhotoDetailResponse>>

    suspend fun likePhoto(photoId: String): Flow<Source<Unit>>

    suspend fun unlikePhoto(photoId: String): Flow<Source<Unit>>

    suspend fun getCurrentUsername(): Flow<Source<CurrentUserResponse>>

    suspend fun searchPhotos(request: SearchPhotosRequest): Flow<Source<SearchPhotosResponse>>

    fun storeAuthorizationHeader(value: String)

    fun clearAuthorizationHeader()

    suspend fun storeDarkModeConfig(value: Boolean)

    fun getCurrentDarkModeConfig(): Flow<Boolean>

    suspend fun storeCurrentUser(user: StoredUser)

    suspend fun getCurrentUser(): Flow<StoredUser>


}