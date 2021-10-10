package br.com.alaksion.myapplication.domain.usecase

import android.net.Uri
import br.com.alaksion.myapplication.config.AuthConfig

class GetAuthUrlUseCase(private val getApiKeyUseCase: GetApiKeyUseCase) {

    operator fun invoke(): Uri {
        val params =
            "client_id=${getApiKeyUseCase()}&redirect_uri=${AuthConfig.REDIRECT_URL}&response_type=${AuthConfig.RESPONSE_TYPE}&scope=${AuthConfig.SCOPE}"
        return Uri.parse("${AuthConfig.AUTH_FLOW_URL}?$params")
    }

}