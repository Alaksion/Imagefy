package br.com.alaksion.myapplication.data.model.photo

import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import com.google.gson.annotations.SerializedName

data class PhotoData(
    val id: String?,
    val likes: Int?,
    val description: String?,
    val user: PhotoOwnerData?,
    val urls: PhotoUrlsData?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean
)

fun PhotoData.mapToDomain() = PhotoResponse(
    id = this.id.handleOptional(),
    authorName = this.user?.name.handleOptional(),
    authorUserName = this.user?.username.handleOptional(),
    authorProfileThumbUrl = this.user?.profileImage?.large.handleOptional(),
    description = this.description.handleOptional(),
    likes = this.likes.handleOptional(),
    photoUrl = this.urls?.regular.handleOptional(),
    likedByUser = this.likedByUser
)