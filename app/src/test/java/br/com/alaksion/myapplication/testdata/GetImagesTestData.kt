package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.photo.*

object GetImagesTestData {

    val DATA_RESPONSE = listOf<PhotoData>(
        PhotoData(
            urls = PhotoUrlsData("", "", ""),
            user = PhotoOwnerData("", "", PhotoOwnerProfileImageData("")),
            id = "",
            likes = 1,
            likedByUser = false,
            description = ""
        )
    )

    val DOMAIN_RESPONSE = DATA_RESPONSE.map { item -> item.mapToDomain() }

}