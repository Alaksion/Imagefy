package br.com.alaksion.myapplication.domain.usecase

import javax.inject.Inject

class GetClientIdUseCase @Inject constructor(
    private val getApiKeyUseCase: GetApiKeyUseCase
) {

    operator fun invoke(): String {
        return "Client-ID ${getApiKeyUseCase()}"
    }

}