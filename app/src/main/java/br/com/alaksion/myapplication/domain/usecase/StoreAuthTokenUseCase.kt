package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import javax.inject.Inject

class StoreAuthTokenUseCase @Inject constructor(
    private val repository: UnsplashRepository
) {

    operator fun invoke(value: String) {
        repository.storeAuthorizationHeader(value)
    }

}