package br.com.alaksion.network.client.domain.usecase

import android.content.SharedPreferences
import br.com.alaksion.network.client.NetworkClientConfig
import javax.inject.Inject

class GetAuthorizationHeaderUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    operator fun invoke(): String {
        return sharedPreferences.getString(NetworkClientConfig.AUTH_TOKEN_KEY, "") ?: ""
    }

}