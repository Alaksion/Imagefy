package br.com.alaksion.myapplication.domain.repository

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.*

interface UnsplashRepository {

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

}