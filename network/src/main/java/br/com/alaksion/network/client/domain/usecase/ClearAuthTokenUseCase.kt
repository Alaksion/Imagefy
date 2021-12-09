package br.com.alaksion.network.client.domain.usecase

import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import javax.inject.Inject

class ClearAuthTokenUseCase @Inject constructor(
    private val repository: NetworkClientRepository
) {

    operator fun invoke() {
        repository.clearAuthorizationHeader()
    }

}