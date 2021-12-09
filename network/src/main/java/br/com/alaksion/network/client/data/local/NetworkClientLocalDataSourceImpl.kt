package br.com.alaksion.network.client.data.local

import android.content.SharedPreferences
import br.com.alaksion.network.client.NetworkClientConfig
import br.com.alaksion.network.client.data.datasource.NetworkClientLocalDataSource
import javax.inject.Inject

class NetworkClientLocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : NetworkClientLocalDataSource {

    override fun storeAuthorizationHeader(value: String) {
        sharedPreferences.edit().putString(NetworkClientConfig.AUTH_TOKEN_KEY, value).apply()
    }

    override fun clearAuthorizationHeader() {
        sharedPreferences.edit().remove(NetworkClientConfig.AUTH_TOKEN_KEY).apply()
    }

}