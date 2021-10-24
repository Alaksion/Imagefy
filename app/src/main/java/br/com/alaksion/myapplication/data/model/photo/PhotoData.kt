package br.com.alaksion.myapplication.data.model.photo

import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import com.google.gson.annotations.SerializedName

data class PhotoData(
    val id: String?,
    val likes: Int?,
    val description: String?,
    val downloads: Int?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean?,
    val user: PhotoOwnerData?,
    val urls: PhotoUrlsData?,
    val links: PhotoLinksData?
)

fun PhotoData.mapToPhotoResponse() = PhotoResponse(
    id = this.id.handleOptional(),
    authorName = this.user?.name.handleOptional(),
    authorUserName = this.user?.username.handleOptional(),
    authorProfileThumbUrl = this.user?.profileImage?.large.handleOptional(),
    description = this.description.handleOptional(),
    likes = this.likes.handleOptional(),
    photoUrl = this.urls?.regular.handleOptional(),
    likedByUser = this.likedByUser.handleOptional()
)

fun PhotoData.mapToPhotoDetailResponse() = PhotoDetailResponse(
    authorName = this.user?.name.handleOptional(),
    imageUrl = this.urls?.full.handleOptional(),
    downloadLink = this.links?.download.handleOptional(),
    downloads = this.downloads.handleOptional(),
    likedByUser = this.likedByUser.handleOptional(),
    likes = this.likes.handleOptional(),
    id = this.id.handleOptional(),
    authorProfileImage = this.user?.profileImage?.large.handleOptional()
)

fun PhotoData.mapToAuthorPhotoResponse() = AuthorPhotosResponse(
    photoId = this.id.handleOptional(),
    photoUrl = this.urls?.regular.handleOptional()
)