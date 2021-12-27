package br.com.alaksion.network.client.domain.usecase

import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import javax.inject.Inject

class GetAuthorizationHeaderUseCase @Inject constructor(
    private val repository: NetworkClientRepository
) {

    operator fun invoke(): String {
        return repository.getAuthorizationHeader()
    }

}