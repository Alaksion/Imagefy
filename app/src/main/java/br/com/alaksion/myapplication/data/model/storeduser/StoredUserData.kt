package br.com.alaksion.myapplication.data.model.storeduser

import br.com.alaksion.myapplication.domain.model.StoredUser

data class StoredUserData(
    val userName: String,
    val name: String,
    val followersCount: Int,
    val followingCount: Int,
    val profileImageUrl: String
)

fun StoredUserData.mapToDomain() = StoredUser(
    userName = this.userName,
    name = this.name,
    followersCount = this.followersCount,
    followingCount = this.followingCount,
    profileImageUrl = this.profileImageUrl
)

fun StoredUser.mapToData() = StoredUserData(
    userName = this.userName,
    name = this.name,
    followersCount = this.followersCount,
    followingCount = this.followingCount,
    profileImageUrl = this.profileImageUrl
)
