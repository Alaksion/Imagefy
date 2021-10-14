package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.common.network.handleApiResponse
import br.com.alaksion.myapplication.data.datasource.ImagefyRemoteDataSource
import br.com.alaksion.myapplication.data.model.auth.AuthValidationResponseData
import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.authorphotos.AuthorPhotoData
import br.com.alaksion.myapplication.data.model.currentuser.CurrentUserResponseData
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.photodetails.PhotoDetailsData
import br.com.alaksion.myapplication.data.remote.services.UnsplashAuthService
import br.com.alaksion.myapplication.data.remote.services.UnsplashService
import retrofit2.Response
import javax.inject.Inject

class ImagefyRemoteDataSourceImpl @Inject constructor(
    private val service: UnsplashService,
    private val authService: UnsplashAuthService
) : ImagefyRemoteDataSource {

    override suspend fun validateLogin(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        authCode: String,
        grantType: String
    ): Source<AuthValidationResponseData> {
        return authService.validateLogin(clientId, clientSecret, redirectUri, authCode, grantType)
            .handleApiResponse()
    }

    override suspend fun getPhotos(page: Int): Source<List<PhotoData>> {
        return service.getPhotos(page).handleApiResponse()
    }

    override suspend fun getAuthorProfile(userName: String): Source<UserResponseData> {
        return service.getAuthorProfile(userName).handleApiResponse()
    }

    override suspend fun getAuthorPhotos(
        userName: String,
        page: Int
    ): Source<List<AuthorPhotoData>> {
        return service.getAuthorPhotos(username = userName, page = page).handleApiResponse()
    }

    override suspend fun getPhotoDetails(photoId: String): Source<PhotoDetailsData> {
        return service.getPhotoDetails(photoId).handleApiResponse()
    }

    override suspend fun likePhoto(photoId: String): Source<Unit> {
        return service.likePhoto(photoId).handleApiResponse()
    }

    override suspend fun unlikePhoto(photoId: String): Source<Unit> {
        return service.unlikePhoto(photoId).handleApiResponse()
    }

    override suspend fun getCurrentUsername(): Source<CurrentUserResponseData> {
        return service.getCurrentUsername().handleApiResponse()
    }

}