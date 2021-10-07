package br.com.alaksion.myapplication.domain.model

data class PhotoDetailResponse(
    val imageUrl: String,
    val downloadLink: String,
    val id: String,
    val authorName: String,
    val authorProfileImage: String,
    val likes: Int
)