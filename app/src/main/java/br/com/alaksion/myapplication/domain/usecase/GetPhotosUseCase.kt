package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: UnsplashRepository
) {

    suspend operator fun invoke(page: Int): Source<List<PhotoResponse>> {
        return repository.getPhotos(page)
    }

}