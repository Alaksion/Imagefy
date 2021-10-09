package br.com.alaksion.myapplication.domain.usecase

import android.net.Uri
import br.com.alaksion.myapplication.config.AuthConfig

class GetAuthUrlUseCase(private val getApiKeyUseCase: GetApiKeyUseCase) {

    operator fun invoke(): Uri {
        val baseUri = Uri.parse(AuthConfig.AUTH_FLOW_URL)
        baseUri.buildUpon()
            .appendQueryParameter("client_id", getApiKeyUseCase())
            .appendQueryParameter("redirect_uri", AuthConfig.REDIRECT_URL)
            .appendQueryParameter("response_type", AuthConfig.RESPONSE_TYPE)
            .appendQueryParameter("scope", AuthConfig.SCOPE)
            .build()
        return baseUri
    }

}