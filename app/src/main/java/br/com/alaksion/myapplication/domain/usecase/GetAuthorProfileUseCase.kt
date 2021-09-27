package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import javax.inject.Inject

class GetAuthorProfileUseCase @Inject constructor(
    private val repository: UnsplashRepository
) {

    suspend operator fun invoke(username: String): Source<AuthorResponse> {
        return repository.getAuthorProfile(username)
    }

}