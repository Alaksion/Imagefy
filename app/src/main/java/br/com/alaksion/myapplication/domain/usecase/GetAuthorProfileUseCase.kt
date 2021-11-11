package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthorProfileUseCase @Inject constructor(
    private val repository: ImagefyRepository
) {

    suspend operator fun invoke(username: String): Flow<Source<AuthorResponse>> {
        return repository.getAuthorProfile(username)
    }

}