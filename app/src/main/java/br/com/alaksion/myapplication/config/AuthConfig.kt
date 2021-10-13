package br.com.alaksion.myapplication.config

import br.com.alaksion.myapplication.BuildConfig

object AuthConfig {
    const val AUTH_FLOW_URL = "https://unsplash.com/oauth/authorize"
    const val REDIRECT_URL = BuildConfig.REDIRECT_URI
    const val SCOPE = BuildConfig.API_SCOPE
    const val RESPONSE_TYPE = "code"
    const val GRANT_TYPE = "authorization_code"
}