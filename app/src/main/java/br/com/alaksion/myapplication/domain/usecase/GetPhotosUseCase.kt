package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.model.Photo
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.network.model.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: ImagefyRepository
) {

    suspend operator fun invoke(page: Int): Flow<Source<List<Photo>>> {
        return repository.getPhotos(page)
    }

}