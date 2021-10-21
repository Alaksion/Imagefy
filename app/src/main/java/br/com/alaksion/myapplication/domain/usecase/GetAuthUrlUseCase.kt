package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.config.AuthConfig

class GetAuthUrlUseCase(private val getApiKeyUseCase: GetApiKeyUseCase) {

    operator fun invoke(): String {
        val params =
            "client_id=${getApiKeyUseCase()}&redirect_uri=${AuthConfig.REDIRECT_URL}&response_type=${AuthConfig.RESPONSE_TYPE}&scope=${AuthConfig.SCOPE}"
        return "${AuthConfig.AUTH_FLOW_URL}?$params"
    }

}