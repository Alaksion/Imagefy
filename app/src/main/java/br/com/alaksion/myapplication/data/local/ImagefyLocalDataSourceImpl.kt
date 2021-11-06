package br.com.alaksion.myapplication.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.data.datasource.ImagefyLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ImagefyLocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) : ImagefyLocalDataSource {

    val dataStore = context.dataStore

    override fun storeAuthorizationHeader(value: String) {
        sharedPreferences.edit().putString(AppLocalConfig.AUTH_TOKEN_KEY, value).apply()
    }

    override fun clearAuthorizationHeader() {
        sharedPreferences.edit().remove(AppLocalConfig.AUTH_TOKEN_KEY).apply()
    }

    override suspend fun storeDarkModeConfig(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[AppLocalConfig.DARK_MODE_KEY] = value
        }
    }

    override suspend fun getCurrentDarkModeConfig(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[AppLocalConfig.DARK_MODE_KEY].handleOptional()
        }
    }

}