package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.network.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: ImagefyRepository
) {

    suspend operator fun invoke(page: Int): Flow<Source<List<PhotoResponse>>> {
        return repository.getPhotos(page)
    }

}