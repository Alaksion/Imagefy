package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import javax.inject.Inject

class RatePhotoUseCase @Inject constructor(
    private val repository: ImagefyRepository
) {

    suspend operator fun invoke(isLike: Boolean, photoId: String): Source<Unit> {
        return if (isLike) repository.likePhoto(photoId) else repository.unlikePhoto(photoId)
    }

}