package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.authorphotos.AuthorPhotoData
import br.com.alaksion.myapplication.data.model.authorphotos.AuthorPhotoUrls

object AuthorPhotosTestData {

    val DATA_RESPONSE = listOf<AuthorPhotoData>(
        AuthorPhotoData("", urls = AuthorPhotoUrls(""))
    )

}