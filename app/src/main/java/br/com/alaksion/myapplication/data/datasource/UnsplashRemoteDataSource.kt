package br.com.alaksion.myapplication.data.datasource

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.author.UserResponseData

interface UnsplashRemoteDataSource {

    suspend fun getPhotos(page: Int): Source<List<PhotoData>>

    suspend fun getAuthorProfile(userName: String): Source<UserResponseData>

}