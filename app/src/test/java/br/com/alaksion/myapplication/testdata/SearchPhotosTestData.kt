package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.photo.*
import br.com.alaksion.myapplication.data.model.searchphotos.SearchPhotosResponseData
import br.com.alaksion.myapplication.data.model.searchphotos.mapToData
import br.com.alaksion.myapplication.data.model.searchphotos.mapToSearchPhotosResponse
import br.com.alaksion.myapplication.domain.model.SearchPhotosRequest

object SearchPhotosTestData {

    val DATA_RESPONSE = SearchPhotosResponseData(
        totalPages = 4,
        results = listOf(
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
        ),
    )
    val DOMAIN_RESPONSE = DATA_RESPONSE.mapToSearchPhotosResponse()

    val DOMAIN_REQUEST = SearchPhotosRequest(1, "value")
    val DATA_REQUEST = DOMAIN_REQUEST.mapToData()
}