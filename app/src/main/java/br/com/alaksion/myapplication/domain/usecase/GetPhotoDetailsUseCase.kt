package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.model.PhotoDetail
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.network.model.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoDetailsUseCase @Inject constructor(private val repository: ImagefyRepository) {

    suspend operator fun invoke(photoId: String): Flow<Source<PhotoDetail>> {
        return repository.getPhotoDetails(photoId)
    }

}