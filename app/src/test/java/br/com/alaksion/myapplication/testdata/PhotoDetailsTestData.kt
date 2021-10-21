package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.photo.PhotoOwnerData
import br.com.alaksion.myapplication.data.model.photo.PhotoOwnerProfileImageData
import br.com.alaksion.myapplication.data.model.photo.PhotoUrlsData
import br.com.alaksion.myapplication.data.model.photodetails.PhotoDetailsData
import br.com.alaksion.myapplication.data.model.photodetails.PhotoLinksData
import br.com.alaksion.myapplication.data.model.photodetails.mapToDomain

object PhotoDetailsTestData {

    val DATA_RESPONSE = PhotoDetailsData(
        likedByUser = false,
        likes = 1,
        id = "",
        user = PhotoOwnerData(
            profileImage = PhotoOwnerProfileImageData(large = ""),
            name = "",
            username = ""
        ),
        urls = PhotoUrlsData("", "", ""),
        downloads = 1,
        links = PhotoLinksData("")
    )

    val DOMAIN_RESPONSE = DATA_RESPONSE.mapToDomain()

}