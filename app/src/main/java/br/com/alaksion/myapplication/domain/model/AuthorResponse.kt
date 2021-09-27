package br.com.alaksion.myapplication.domain.model

data class AuthorResponse(
    val username: String,
    val name: String,
    val instagramUser: String,
    val twitterUser: String,
    val portfolioUrl: String,
    val bio: String,
    val totalLikes: Int,
    val followers: Int,
    val following: Int,
    val totalPhotos: Int,
    val profileImage: String
)