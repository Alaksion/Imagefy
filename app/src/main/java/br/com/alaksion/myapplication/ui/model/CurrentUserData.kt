package br.com.alaksion.myapplication.ui.model

data class CurrentUserData(
    val userName: String? = "Username",
    val name: String? = "Name",
    val followersCount: Int? = 10,
    val followingCount: Int? = 20,
    val profileImageUrl: String? = null
)
