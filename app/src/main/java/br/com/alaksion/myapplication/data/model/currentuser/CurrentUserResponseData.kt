package br.com.alaksion.myapplication.data.model.currentuser

import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.domain.model.CurrentUser

data class CurrentUserResponseData(
    val username: String?
)

fun CurrentUserResponseData.mapToCurrentUserResponse() = CurrentUser(
    username = this.username.handleOptional()
)
