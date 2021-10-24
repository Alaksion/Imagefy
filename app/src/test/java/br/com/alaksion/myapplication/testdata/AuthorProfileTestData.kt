package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.author.AuthorImageResponseData
import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.author.mapToAuthorResponse

object AuthorProfileTestData {

    val DATA_RESPONSE = UserResponseData(
        id = "",
        username = "",
        name = "",
        twitterUsername = "",
        portfolioUrl = "",
        instagramUser = "",
        totalPhotos = 1,
        totalLikes = 1,
        profileImage = AuthorImageResponseData(
            small = "",
            medium = "",
            large = ""
        ),
        bio = "",
        followingCount = 1,
        followersCount = 1,
        location = "",
        updatedAt = ""
    )

    val DOMAIN_RESPONSE = DATA_RESPONSE.mapToAuthorResponse()

}