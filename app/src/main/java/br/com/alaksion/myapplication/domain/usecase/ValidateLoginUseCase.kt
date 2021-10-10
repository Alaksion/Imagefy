package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.config.AuthConfig
import br.com.alaksion.myapplication.domain.model.AuthResponse
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import javax.inject.Inject

class ValidateLoginUseCase @Inject constructor(
    private val repository: ImagefyRepository,
    private val getApiKeyUseCase: GetApiKeyUseCase,
    private val getApiSecretKeyUseCase: GetApiSecretKeyUseCase
) {

    suspend operator fun invoke(authCode: String): Source<AuthResponse> {
        return repository.validateLogin(
            clientId = getApiKeyUseCase(),
            clientSecret = getApiSecretKeyUseCase(),
            redirectUri = AuthConfig.REDIRECT_URL,
            grantType = AuthConfig.GRANT_TYPE,
            authCode = authCode
        )
    }

}