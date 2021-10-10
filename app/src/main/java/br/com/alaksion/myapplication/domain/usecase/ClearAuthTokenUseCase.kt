package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import javax.inject.Inject

class ClearAuthTokenUseCase @Inject constructor(
    private val repository: UnsplashRepository
) {

    operator fun invoke() {
        repository.clearAuthorizationHeader()
    }

}