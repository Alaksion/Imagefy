package br.com.alaksion.network.client.domain.usecase

import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import javax.inject.Inject

class StoreAuthTokenUseCase @Inject constructor(
    private val repository: NetworkClientRepository
) {

    operator fun invoke(value: String) {
        repository.storeAuthorizationHeader(value)
    }

}