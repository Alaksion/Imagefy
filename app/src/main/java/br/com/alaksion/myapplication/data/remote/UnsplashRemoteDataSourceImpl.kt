package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.common.network.handleApiResponse
import br.com.alaksion.myapplication.data.datasource.UnsplashRemoteDataSource
import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.authorphotos.AuthorPhotoData
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.photodetails.PhotoDetailsData
import javax.inject.Inject

class UnsplashRemoteDataSourceImpl @Inject constructor(
    private val api: UnsplashApi
) : UnsplashRemoteDataSource {

    override suspend fun getPhotos(page: Int): Source<List<PhotoData>> {
        return api.getPhotos(page).handleApiResponse()
    }

    override suspend fun getAuthorProfile(userName: String): Source<UserResponseData> {
        return api.getAuthorProfile(userName).handleApiResponse()
    }

    override suspend fun getAuthorPhotos(
        userName: String,
        page: Int
    ): Source<List<AuthorPhotoData>> {
        return api.getAuthorPhotos(username = userName, page = page).handleApiResponse()
    }

    override suspend fun getPhotoDetails(photoId: String): Source<PhotoDetailsData> {
        return api.getPhotoDetails(photoId).handleApiResponse()
    }

}