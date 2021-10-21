package br.com.alaksion.myapplication.data.model.photodetails

import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.data.model.photo.PhotoOwnerData
import br.com.alaksion.myapplication.data.model.photo.PhotoUrlsData
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import com.google.gson.annotations.SerializedName

data class PhotoDetailsData(
    val id: String?,
    val user: PhotoOwnerData?,
    val links: PhotoLinksData?,
    val urls: PhotoUrlsData?,
    val likes: Int?,
    val downloads: Int?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean
)

fun PhotoDetailsData.mapToDomain() = PhotoDetailResponse(
    imageUrl = this.urls?.full.handleOptional(),
    downloadLink = this.links?.download.handleOptional(),
    id = this.id.handleOptional(),
    authorName = this.user?.username.handleOptional(),
    authorProfileImage = this.user?.profileImage?.large.handleOptional(),
    likes = this.likes.handleOptional(),
    downloads = this.downloads.handleOptional(),
    likedByUser = this.likedByUser
)
