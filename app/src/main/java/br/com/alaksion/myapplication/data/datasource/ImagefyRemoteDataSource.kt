package br.com.alaksion.myapplication.data.datasource

import br.com.alaksion.myapplication.data.model.auth.AuthValidationResponseData
import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.currentuser.CurrentUserResponseData
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.searchphotos.SearchPhotosRequestData
import br.com.alaksion.myapplication.data.model.searchphotos.SearchPhotosResponseData
import br.com.alaksion.network.Source
import kotlinx.coroutines.flow.Flow

interface ImagefyRemoteDataSource {

    suspend fun validateLogin(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        authCode: String,
        grantType: String
    ): Flow<Source<AuthValidationResponseData>>

    suspend fun getPhotos(page: Int): Flow<Source<List<PhotoData>>>

    suspend fun getAuthorProfile(userName: String): Flow<Source<UserResponseData>>

    suspend fun getAuthorPhotos(userName: String, page: Int): Flow<Source<List<PhotoData>>>

    suspend fun getPhotoDetails(photoId: String): Flow<Source<PhotoData>>

    suspend fun likePhoto(photoId: String): Flow<Source<Unit>>

    suspend fun unlikePhoto(photoId: String): Flow<Source<Unit>>

    suspend fun getCurrentUsername(): Flow<Source<CurrentUserResponseData>>

    suspend fun searchPhotos(request: SearchPhotosRequestData): Flow<Source<SearchPhotosResponseData>>
}