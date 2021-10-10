package br.com.alaksion.myapplication.config

object AuthConfig {
    const val AUTH_FLOW_URL = "https://unsplash.com/oauth/authorize"
    const val REDIRECT_URL = "https://imagefy.com.br/auth-callback"
    const val SCOPE = "public+read_user+write_likes"
    const val RESPONSE_TYPE = "code"
    const val GRANT_TYPE = "authorization_code"
}