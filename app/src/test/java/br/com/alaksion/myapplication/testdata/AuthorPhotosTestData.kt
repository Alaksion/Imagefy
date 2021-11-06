package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.photo.*

object AuthorPhotosTestData {

    val DATA_RESPONSE = listOf(
        PhotoData(
            urls = PhotoUrlsData("", "", ""),
            user = PhotoOwnerData("", "", PhotoOwnerProfileImageData("")),
            id = "",
            likes = 1,
            likedByUser = false,
            description = "",
            downloads = 100,
            links = PhotoLinksData(""),
            color = ""
        )
    )

    val DOMAIN_RESPONSE = DATA_RESPONSE.map { item -> item.mapToAuthorPhotoResponse() }

}