package br.com.alaksion.myapplication.domain.repository

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.*

interface ImagefyRepository {

    suspend fun validateLogin(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        authCode: String,
        grantType: String
    ): Source<AuthResponse>

    suspend fun getPhotos(page: Int): Source<List<PhotoResponse>>

    suspend fun getAuthorProfile(username: String): Source<AuthorResponse>

    suspend fun getAuthorPhotos(username: String, page: Int): Source<List<AuthorPhotosResponse>>

    suspend fun getPhotoDetails(photoId: String): Source<PhotoDetailResponse>

    suspend fun likePhoto(photoId: String): Source<Unit>

    suspend fun unlikePhoto(photoId: String): Source<Unit>

    fun storeAuthorizationHeader(value: String)

    fun clearAuthorizationHeader()

}