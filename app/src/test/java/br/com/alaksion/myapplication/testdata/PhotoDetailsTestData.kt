package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.photo.*

object PhotoDetailsTestData {

    val DATA_RESPONSE = PhotoData(
        urls = PhotoUrlsData("", "", ""),
        user = PhotoOwnerData("", "", PhotoOwnerProfileImageData("")),
        id = "",
        likes = 1,
        likedByUser = false,
        description = "",
        downloads = 100,
        links = PhotoLinksData("")
    )

    val DOMAIN_RESPONSE = DATA_RESPONSE.mapToPhotoDetailResponse()

}