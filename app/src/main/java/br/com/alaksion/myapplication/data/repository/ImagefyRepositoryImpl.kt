package br.com.alaksion.myapplication.data.repository

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
import br.com.alaksion.myapplication.data.model.storeduser.mapToData
import br.com.alaksion.myapplication.data.model.storeduser.mapToDomain
import br.com.alaksion.myapplication.domain.model.*
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.network.Source
import br.com.alaksion.network.mapSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    ): Flow<Source<Auth>> {
        return remoteDataSource.validateLogin(
            clientId,
            clientSecret,
            redirectUri,
            authCode,
            grantType
        ).map { it.mapSource { response -> response?.mapToAuthResponse() } }
    }

    override suspend fun getPhotos(page: Int): Flow<Source<List<Photo>>> {
        return remoteDataSource.getPhotos(page).map { source ->
            source.mapSource { photos -> photos?.map { photo -> photo.mapToPhotoResponse() } }
        }
    }

    override suspend fun getAuthorProfile(username: String): Flow<Source<Author>> {
        return remoteDataSource.getAuthorProfile(username).map { source ->
            source.mapSource { userData -> userData?.mapToAuthorResponse() }
        }

    }

    override suspend fun getAuthorPhotos(
        username: String,
        page: Int
    ): Flow<Source<List<AuthorPhotos>>> {
        return remoteDataSource.getAuthorPhotos(userName = username, page = page).map { source ->
            source.mapSource { photos -> photos?.map { item -> item.mapToAuthorPhotoResponse() } }
        }
    }

    override suspend fun getPhotoDetails(photoId: String): Flow<Source<PhotoDetail>> {
        return remoteDataSource.getPhotoDetails(photoId).map { source ->
            source.mapSource { it?.mapToPhotoDetailResponse() }
        }
    }

    override suspend fun likePhoto(photoId: String): Flow<Source<Unit>> {
        return remoteDataSource.likePhoto(photoId).map { source -> source.mapSource { } }
    }

    override suspend fun unlikePhoto(photoId: String): Flow<Source<Unit>> {
        return remoteDataSource.unlikePhoto(photoId).map { source -> source.mapSource { } }
    }

    override suspend fun getCurrentUsername(): Flow<Source<CurrentUser>> {
        return remoteDataSource.getCurrentUsername().map { source ->
            source.mapSource { it?.mapToCurrentUserResponse() }
        }
    }

    override suspend fun searchPhotos(request: SearchPhotosRequest): Flow<Source<SearchPhotos>> {
        return remoteDataSource.searchPhotos(request.mapToData()).map { source ->
            source.mapSource { it?.mapToSearchPhotosResponse() }
        }
    }

    override suspend fun storeDarkModeConfig(value: Boolean) {
        localDataSource.storeDarkModeConfig(value)
    }

    override fun getCurrentDarkModeConfig(): Flow<Boolean> {
        return localDataSource.getCurrentDarkModeConfig()
    }

    override suspend fun storeCurrentUser(user: StoredUser) {
        localDataSource.storeCurrentUser(user.mapToData())
    }

    override suspend fun getCurrentUser(): Flow<StoredUser> {
        return localDataSource.getCurrentUser().map { it.mapToDomain() }
    }

}