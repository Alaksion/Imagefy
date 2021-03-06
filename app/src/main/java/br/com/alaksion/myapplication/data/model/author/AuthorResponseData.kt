package br.com.alaksion.myapplication.data.model.author

import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.domain.model.Author
import com.google.gson.annotations.SerializedName

data class UserResponseData(
    val id: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val username: String?,
    val name: String?,
    @SerializedName("instagram_username")
    val instagramUser: String?,
    @SerializedName("twitter_username")
    val twitterUsername: String?,
    @SerializedName("portfolio_url")
    val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    @SerializedName("total_likes")
    val totalLikes: Int?,
    @SerializedName("total_photos")
    val totalPhotos: Int?,
    @SerializedName("followers_count")
    val followersCount: Int?,
    @SerializedName("following_count")
    val followingCount: Int?,
    @SerializedName("profile_image")
    val profileImage: AuthorImageResponseData?,
    @SerializedName("followed_by_user")
    val followedByUser: Boolean?
)

fun UserResponseData.mapToAuthorResponse() = Author(
    username = this.username.handleOptional(),
    name = this.name.handleOptional(),
    instagramUser = this.instagramUser.handleOptional(),
    twitterUser = this.twitterUsername.handleOptional(),
    bio = this.bio.handleOptional(),
    followers = this.followersCount.handleOptional(),
    following = this.followingCount.handleOptional(),
    totalPhotos = this.totalPhotos.handleOptional(),
    totalLikes = this.totalLikes.handleOptional(),
    profileImage = this.profileImage?.large.handleOptional(),
    portfolioUrl = this.portfolioUrl.handleOptional(),
    followedByUser = this.followedByUser.handleOptional()
)
