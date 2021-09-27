package br.com.alaksion.myapplication.data.model.photo

import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import com.google.gson.annotations.SerializedName

data class PhotoData(
    val id: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    val width: Int?,
    val height: Int?,
    val color: String?,
    @SerializedName("blur_hash")
    val blurHash: String?,
    val likes: Int?,
    val description: String?,
    val user: PhotoOwnerData?,
    val urls: PhotoUrlsData?
)

fun PhotoData.mapToDomain() = PhotoResponse(
    id = this.id.handleOptional(),
    createdAt = this.createdAt.handleOptional(),
    authorName = this.user?.name.handleOptional(),
    authorUserName = this.user?.username.handleOptional(),
    authorProfileThumbUrl = this.user?.profileImage?.large.handleOptional(),
    description = this.description.handleOptional(),
    likes = this.likes.handleOptional(),
    photoUrl = this.urls?.regular.handleOptional()
)