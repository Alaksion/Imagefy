package br.com.alaksion.myapplication.domain.model

data class PhotoDetail(
    val imageUrl: String,
    val downloadLink: String,
    val id: String,
    val authorName: String,
    val authorProfileImage: String,
    val likes: Int,
    val downloads: Int,
    val likedByUser: Boolean,
    val color: String
)