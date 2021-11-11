package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoDetailsUseCase @Inject constructor(private val repository: ImagefyRepository) {

    suspend operator fun invoke(photoId: String): Flow<Source<PhotoDetailResponse>> {
        return repository.getPhotoDetails(photoId)
    }

}