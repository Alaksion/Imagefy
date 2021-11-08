package br.com.alaksion.myapplication.data.repository

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.common.network.mapSource
import br.com.alaksion.myapplication.data.datasource.ImagefyLocalDataSource
import br.com.alaksion.myapplication.data.datasource.ImagefyRemoteDataSource
import br.com.alaksion.myapplication.data.model.auth.mapToAuthResponse
import br.com.alaksion.myapplication.data.model.author.mapToAuthorResponse
import br.com.alaksion.myapplication.data.model.currentuser.mapToCurrentUserResponse
import br.com.alaksion.myapplication.data.model.photo.mapToAuthorPhotoResponse
import br.com.alaksion.myapplication.data.model.photo.mapToPhotoDetailResponse
import br.com.alaksion.myapplication.data.model.photo.mapToPhotoResponse
import br.com.alaksion.myapplication.data.model.searchphotos.mapToData
import br.com.alaksion.myapplication.data.model.searchphotos.mapToSearchPhotosResponse
import br.com.alaksion.myapplication.domain.model.*
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImagefyRepositoryImpl @Inject constructor(
    private val remoteDataSource: ImagefyRemoteDataSource,
    private val localDataSource: ImagefyLocalDataSource
) : ImagefyRepository {

    override suspend fun validateLogin(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        authCode: String,
        grantType: String
    ): Source<AuthResponse> {
        return remoteDataSource.validateLogin(
            clientId,
            clientSecret,
            redirectUri,
            authCode,
            grantType
        ).mapSource { it?.mapToAuthResponse() }
    }

    override suspend fun getPhotos(page: Int): Source<List<PhotoResponse>> {
        return remoteDataSource.getPhotos(page)
            .mapSource { photos -> photos?.map { photo -> photo.mapToPhotoResponse() } }
    }

    override suspend fun getAuthorProfile(username: String): Source<AuthorResponse> {
        return remoteDataSource.getAuthorProfile(username)
            .mapSource { userData -> userData?.mapToAuthorResponse() }
    }

    override suspend fun getAuthorPhotos(
        username: String,
        page: Int
    ): Source<List<AuthorPhotosResponse>> {
        return remoteDataSource.getAuthorPhotos(userName = username, page = page)
            .mapSource { photos -> photos?.map { item -> item.mapToAuthorPhotoResponse() } }
    }

    override suspend fun getPhotoDetails(photoId: String): Source<PhotoDetailResponse> {
        return remoteDataSource.getPhotoDetails(photoId)
            .mapSource { it?.mapToPhotoDetailResponse() }
    }

    override suspend fun likePhoto(photoId: String): Source<Unit> {
        return remoteDataSource.likePhoto(photoId)
    }

    override suspend fun unlikePhoto(photoId: String): Source<Unit> {
        return remoteDataSource.unlikePhoto(photoId)
    }

    override suspend fun getCurrentUsername(): Source<CurrentUserResponse> {
        return remoteDataSource.getCurrentUsername().mapSource { it?.mapToCurrentUserResponse() }
    }

    override fun storeAuthorizationHeader(value: String) {
        localDataSource.storeAuthorizationHeader(value)
    }

    override fun clearAuthorizationHeader() {
        localDataSource.clearAuthorizationHeader()
    }

    override suspend fun searchPhotos(request: SearchPhotosRequest): Source<SearchPhotosResponse> {
        return remoteDataSource.searchPhotos(request.mapToData())
            .mapSource { it?.mapToSearchPhotosResponse() }
    }

    override suspend fun storeDarkModeConfig(value: Boolean) {
        localDataSource.storeDarkModeConfig(value)
    }

    override fun getCurrentDarkModeConfig(): Flow<Boolean> {
        return localDataSource.getCurrentDarkModeConfig()
    }

}