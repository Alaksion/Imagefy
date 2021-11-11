package br.com.alaksion.myapplication.data.remote.services

import br.com.alaksion.myapplication.data.model.auth.AuthValidationResponseData
import br.com.alaksion.myapplication.data.remote.UnsplashServiceCompanion
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface UnsplashAuthService {

    @POST("oauth/token")
    suspend fun validateLogin(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("code") authCode: String,
        @Query("grant_type") grantType: String
    ): Response<AuthValidationResponseData>

    companion object : UnsplashServiceCompanion<UnsplashAuthService>()

}