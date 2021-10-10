package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import javax.inject.Inject

class GetAuthorProfileUseCase @Inject constructor(
    private val repository: ImagefyRepository
) {

    suspend operator fun invoke(username: String): Source<AuthorResponse> {
        return repository.getAuthorProfile(username)
    }

}