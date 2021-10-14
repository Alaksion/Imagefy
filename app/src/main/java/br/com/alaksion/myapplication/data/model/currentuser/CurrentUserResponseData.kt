package br.com.alaksion.myapplication.data.model.currentuser

import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.domain.model.CurrentUserResponse

data class CurrentUserResponseData(
    val username: String?
)

fun CurrentUserResponseData.mapToDomain() = CurrentUserResponse(
    username = this.username.handleOptional()
)
