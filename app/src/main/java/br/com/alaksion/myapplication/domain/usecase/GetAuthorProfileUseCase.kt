package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.model.Author
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.network.model.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthorProfileUseCase @Inject constructor(
    private val repository: ImagefyRepository
) {

    suspend operator fun invoke(username: String): Flow<Source<Author>> {
        return repository.getAuthorProfile(username)
    }

}