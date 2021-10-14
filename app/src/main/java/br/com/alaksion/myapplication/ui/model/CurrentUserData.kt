package br.com.alaksion.myapplication.ui.model

data class CurrentUserData(
    var userName: String = "",
    var name: String = "",
    var followersCount: Int = 0,
    var followingCount: Int = 0,
    var profileImageUrl: String = ""
)
