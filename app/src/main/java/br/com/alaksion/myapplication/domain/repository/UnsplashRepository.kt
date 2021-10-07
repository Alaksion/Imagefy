package br.com.alaksion.myapplication.domain.repository

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.domain.model.PhotoResponse

interface UnsplashRepository {

    suspend fun getPhotos(page: Int): Source<List<PhotoResponse>>

    suspend fun getAuthorProfile(username: String): Source<AuthorResponse>

    suspend fun getAuthorPhotos(username: String, page: Int): Source<List<AuthorPhotosResponse>>

    suspend fun getPhotoDetails(photoId: String): Source<PhotoDetailResponse>

}