package br.com.alaksion.myapplication.data.datasource

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.data.model.auth.AuthValidationResponseData
import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.authorphotos.AuthorPhotoData
import br.com.alaksion.myapplication.data.model.currentuser.CurrentUserResponseData
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.photodetails.PhotoDetailsData
import retrofit2.Response

interface ImagefyRemoteDataSource {

    suspend fun validateLogin(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        authCode: String,
        grantType: String
    ): Source<AuthValidationResponseData>

    suspend fun getPhotos(page: Int): Source<List<PhotoData>>

    suspend fun getAuthorProfile(userName: String): Source<UserResponseData>

    suspend fun getAuthorPhotos(userName: String, page: Int): Source<List<AuthorPhotoData>>

    suspend fun getPhotoDetails(photoId: String): Source<PhotoDetailsData>

    suspend fun likePhoto(photoId: String): Source<Unit>

    suspend fun unlikePhoto(photoId: String): Source<Unit>

    suspend fun getCurrentUsername(): Source<CurrentUserResponseData>
}