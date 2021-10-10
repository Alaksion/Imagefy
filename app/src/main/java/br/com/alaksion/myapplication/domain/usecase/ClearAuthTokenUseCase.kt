package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import javax.inject.Inject

class ClearAuthTokenUseCase @Inject constructor(
    private val repository: ImagefyRepository
) {

    operator fun invoke() {
        repository.clearAuthorizationHeader()
    }

}