package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.model.SearchPhotosRequest
import br.com.alaksion.myapplication.domain.model.SearchPhotosResponse
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.network.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPhotosUseCase @Inject constructor(private val repository: ImagefyRepository) {

    suspend operator fun invoke(
        page: Int,
        searchQuery: String
    ): Flow<Source<SearchPhotosResponse>> {
        return repository.searchPhotos(SearchPhotosRequest(page = page, query = searchQuery))
    }

}