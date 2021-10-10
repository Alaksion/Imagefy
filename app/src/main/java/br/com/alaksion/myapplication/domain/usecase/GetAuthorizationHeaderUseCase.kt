package br.com.alaksion.myapplication.domain.usecase

import android.content.SharedPreferences
import br.com.alaksion.myapplication.data.local.AppLocalConfig
import javax.inject.Inject

class GetAuthorizationHeaderUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    operator fun invoke(): String {
        return sharedPreferences.getString(AppLocalConfig.AUTH_TOKEN_KEY, "") ?: ""
    }

}