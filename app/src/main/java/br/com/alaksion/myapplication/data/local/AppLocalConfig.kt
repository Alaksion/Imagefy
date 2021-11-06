package br.com.alaksion.myapplication.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey

object AppLocalConfig {
    const val SHARED_PREFS_KEY = "imagefy_shared_prefs"
    const val AUTH_TOKEN_KEY = "imagefy_auth_token"
    val DARK_MODE_KEY = booleanPreferencesKey("imagefy_darkmode")
}