package br.com.alaksion.myapplication.data.local

import android.content.SharedPreferences
import br.com.alaksion.myapplication.data.datasource.ImagefyLocalDataSource
import javax.inject.Inject

class ImagefyLocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ImagefyLocalDataSource {

    override fun storeAuthorizationHeader(value: String) {
        sharedPreferences.edit().putString(AppLocalConfig.AUTH_TOKEN_KEY, value).apply()
    }

    override fun clearAuthorizationHeader() {
        sharedPreferences.edit().remove(AppLocalConfig.AUTH_TOKEN_KEY).apply()
    }

}