package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.photo.PhotoOwnerData
import br.com.alaksion.myapplication.data.model.photo.PhotoOwnerProfileImageData
import br.com.alaksion.myapplication.data.model.photo.PhotoUrlsData

object GetImagesTestData {

    val DATA_RESPONSE = listOf<PhotoData>(
        PhotoData(
            urls = PhotoUrlsData("", "", ""),
            user = PhotoOwnerData("", "", PhotoOwnerProfileImageData("")),
            id = "",
            likes = 1,
            likedByUser = false,
            color = "",
            width = 1,
            description = "",
            createdAt = "",
            blurHash = "",
            height = 1
        )
    )

}