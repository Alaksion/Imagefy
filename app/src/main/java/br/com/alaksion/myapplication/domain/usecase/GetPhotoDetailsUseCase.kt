package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import javax.inject.Inject

class GetPhotoDetailsUseCase @Inject constructor(private val repository: UnsplashRepository) {

    suspend operator fun invoke(photoId: String): Source<PhotoDetailResponse> {
        return repository.getPhotoDetails(photoId)
    }

}