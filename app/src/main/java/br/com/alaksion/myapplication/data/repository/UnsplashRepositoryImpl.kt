package br.com.alaksion.myapplication.data.repository

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.common.network.mapSource
import br.com.alaksion.myapplication.data.datasource.UnsplashRemoteDataSource
import br.com.alaksion.myapplication.data.model.auth.mapToDomain
import br.com.alaksion.myapplication.data.model.author.mapToDomain
import br.com.alaksion.myapplication.data.model.authorphotos.mapToDomain
import br.com.alaksion.myapplication.data.model.photo.mapToDomain
import br.com.alaksion.myapplication.data.model.photodetails.mapToDomain
import br.com.alaksion.myapplication.domain.model.*
import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import javax.inject.Inject

class UnsplashRepositoryImpl @Inject constructor(
    private val remoteDataSource: UnsplashRemoteDataSource
) : UnsplashRepository {

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
        ).mapSource { it?.mapToDomain() }
    }

    override suspend fun getPhotos(page: Int): Source<List<PhotoResponse>> {
        return remoteDataSource.getPhotos(page)
            .mapSource { photos -> photos?.map { photo -> photo.mapToDomain() } }
    }

    override suspend fun getAuthorProfile(username: String): Source<AuthorResponse> {
        return remoteDataSource.getAuthorProfile(username)
            .mapSource { userData -> userData?.mapToDomain() }
    }

    override suspend fun getAuthorPhotos(
        username: String,
        page: Int
    ): Source<List<AuthorPhotosResponse>> {
        return remoteDataSource.getAuthorPhotos(userName = username, page = page)
            .mapSource { photos -> photos?.map { item -> item.mapToDomain() } }
    }

    override suspend fun getPhotoDetails(photoId: String): Source<PhotoDetailResponse> {
        return remoteDataSource.getPhotoDetails(photoId).mapSource { it?.mapToDomain() }
    }

}