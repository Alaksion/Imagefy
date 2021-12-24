package br.com.alaksion.myapplication.data.model.auth

import br.com.alaksion.myapplication.domain.model.Auth
import com.google.gson.annotations.SerializedName

data class AuthValidationResponseData(
    @SerializedName("access_token")
    val accessToken: String
)

fun AuthValidationResponseData.mapToAuthResponse() = Auth(
    accessToken = this.accessToken
)
