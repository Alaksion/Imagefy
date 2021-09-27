package br.com.alaksion.myapplication.domain.repository

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse

interface UnsplashRepository {

    suspend fun getPhotos(page: Int): Source<List<PhotoResponse>>

    suspend fun getAuthorProfile(username: String): Source<AuthorResponse>

}