package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.photo.PhotoOwnerData
import br.com.alaksion.myapplication.data.model.photo.PhotoOwnerProfileImageData
import br.com.alaksion.myapplication.data.model.photo.PhotoUrlsData
import br.com.alaksion.myapplication.data.model.searchphotos.SearchPhotosResponseData
import br.com.alaksion.myapplication.data.model.searchphotos.mapToDomain

object SearchPhotosTestData {

    val DATA_RESPONSE = SearchPhotosResponseData(
        totalPages = 1,
        results = listOf(
            PhotoData(
                "",
                1,
                "",
                PhotoOwnerData("", "", PhotoOwnerProfileImageData("")),
                PhotoUrlsData("", "", ""),
                false,
            )
        ),
    )

    val DOMAIN_RESPONSE = DATA_RESPONSE.mapToDomain()
}