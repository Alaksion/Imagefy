package br.com.alaksion.myapplication.data.model.authorphotos

import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse

data class AuthorPhotoData(
    val id: String?,
    val urls: AuthorPhotoUrls?
)

fun AuthorPhotoData.mapToDomain() = AuthorPhotosResponse(
    photoId = this.id.handleOptional(),
    photoUrl = this.urls?.regular.handleOptional()
)