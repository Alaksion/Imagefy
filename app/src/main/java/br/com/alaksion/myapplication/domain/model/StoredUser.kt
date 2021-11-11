package br.com.alaksion.myapplication.domain.model

data class StoredUser(
    val userName: String = "",
    val name: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val profileImageUrl: String = ""
)
