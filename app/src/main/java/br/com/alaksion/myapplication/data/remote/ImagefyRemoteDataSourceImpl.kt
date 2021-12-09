package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.data.datasource.ImagefyRemoteDataSource
import br.com.alaksion.myapplication.data.model.auth.AuthValidationResponseData
import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.currentuser.CurrentUserResponseData
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.searchphotos.SearchPhotosRequestData
import br.com.alaksion.myapplication.data.model.searchphotos.SearchPhotosResponseData
import br.com.alaksion.myapplication.data.remote.services.UnsplashAuthService
import br.com.alaksion.myapplication.data.remote.services.UnsplashService
import br.com.alaksion.network.Source
import br.com.alaksion.network.utils.handleApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    ): Flow<Source<AuthValidationResponseData>> {
        return flow {
            emit(
                authService.validateLogin(
                    clientId,
                    clientSecret,
                    redirectUri,
                    authCode,
                    grantType
                ).handleApiResponse()
            )
        }
    }

    override suspend fun getPhotos(page: Int): Flow<Source<List<PhotoData>>> {
        return flow {
            emit(service.getPhotos(page).handleApiResponse())
        }
    }

    override suspend fun getAuthorProfile(userName: String): Flow<Source<UserResponseData>> {
        return flow {
            emit(service.getAuthorProfile(userName).handleApiResponse())
        }
    }

    override suspend fun getAuthorPhotos(
        userName: String,
        page: Int
    ): Flow<Source<List<PhotoData>>> {
        return flow {
            emit(
                service.getAuthorPhotos(username = userName, page = page).handleApiResponse()
            )
        }
    }

    override suspend fun getPhotoDetails(photoId: String): Flow<Source<PhotoData>> {
        return flow {
            emit(service.getPhotoDetails(photoId).handleApiResponse())
        }
    }

    override suspend fun likePhoto(photoId: String): Flow<Source<Unit>> {
        return flow {
            emit(service.likePhoto(photoId).handleApiResponse())
        }
    }

    override suspend fun unlikePhoto(photoId: String): Flow<Source<Unit>> {
        return flow {
            emit(service.unlikePhoto(photoId).handleApiResponse())
        }
    }

    override suspend fun getCurrentUsername(): Flow<Source<CurrentUserResponseData>> {
        return flow {
            emit(service.getCurrentUsername().handleApiResponse())
        }
    }

    override suspend fun searchPhotos(request: SearchPhotosRequestData): Flow<Source<SearchPhotosResponseData>> {
        return flow {
            emit(
                service.searchPhotos(
                    searchQuery = request.query,
                    page = request.page
                ).handleApiResponse()
            )
        }
    }
}