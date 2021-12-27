package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.config.AuthConfig
import br.com.alaksion.myapplication.domain.model.Auth
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.network.model.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ValidateLoginUseCase @Inject constructor(
    private val repository: ImagefyRepository,
    private val getApiKeyUseCase: GetApiKeyUseCase,
    private val getApiSecretKeyUseCase: GetApiSecretKeyUseCase
) {

    suspend operator fun invoke(authCode: String): Flow<Source<Auth>> {
        return repository.validateLogin(
            clientId = getApiKeyUseCase(),
            clientSecret = getApiSecretKeyUseCase(),
            redirectUri = AuthConfig.REDIRECT_URL,
            grantType = AuthConfig.GRANT_TYPE,
            authCode = authCode
        )
    }
}