package br.com.alaksion.myapplication.data.model.auth

import br.com.alaksion.myapplication.domain.model.AuthResponse
import com.google.gson.annotations.SerializedName

data class AuthValidationResponseData(
    @SerializedName("access_token")
    val accessToken: String
)

fun AuthValidationResponseData.mapToDomain() = AuthResponse(
    accessToken = this.accessToken
)
