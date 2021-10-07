package br.com.alaksion.myapplication.data.repository

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.common.network.mapSource
import br.com.alaksion.myapplication.data.datasource.UnsplashRemoteDataSource
import br.com.alaksion.myapplication.data.model.author.mapToDomain
import br.com.alaksion.myapplication.data.model.authorphotos.mapToDomain
import br.com.alaksion.myapplication.data.model.photo.mapToDomain
import br.com.alaksion.myapplication.data.model.photodetails.mapToDomain
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import javax.inject.Inject

class UnsplashRepositoryImpl @Inject constructor(
    private val remoteDataSource: UnsplashRemoteDataSource
) : UnsplashRepository {

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