package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.model.AuthorPhotos
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.network.model.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthorPhotosUseCase @Inject constructor(private val repository: ImagefyRepository) {

    suspend operator fun invoke(
        username: String,
        page: Int
    ): Flow<Source<List<AuthorPhotos>>> {
        return repository.getAuthorPhotos(username = username, page = page)
    }

}