package br.com.alaksion.myapplication.domain.model

data class Photo(
    val id: String,
    var likes: Int,
    val description: String,
    val authorName: String,
    val authorUserName: String,
    val authorProfileThumbUrl: String,
    val photoUrl: String,
    var likedByUser: Boolean,
    val color: String
)