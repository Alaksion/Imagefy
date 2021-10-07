package br.com.alaksion.myapplication.data.model.photodetails

import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.data.model.photo.PhotoOwnerData
import br.com.alaksion.myapplication.data.model.photo.PhotoUrlsData
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse

data class PhotoDetailsData(
    val id: String?,
    val user: PhotoOwnerData?,
    val links: PhotoLinksData?,
    val urls: PhotoUrlsData?,
    val likes: Int?
)

fun PhotoDetailsData.mapToDomain() = PhotoDetailResponse(
    imageUrl = this.urls?.raw.handleOptional(),
    downloadLink = this.links?.download.handleOptional(),
    id = this.id.handleOptional(),
    authorName = this.user?.username.handleOptional(),
    authorProfileImage = this.user?.profileImage?.large.handleOptional(),
    likes = this.likes.handleOptional()
)
