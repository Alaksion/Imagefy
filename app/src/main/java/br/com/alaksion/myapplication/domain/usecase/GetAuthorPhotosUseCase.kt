package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import javax.inject.Inject

class GetAuthorPhotosUseCase @Inject constructor(private val repository: UnsplashRepository) {

    suspend operator fun invoke(username: String, page: Int): Source<List<AuthorPhotosResponse>> {
        return repository.getAuthorPhotos(username = username, page = page)
    }

}