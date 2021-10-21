package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.authorphotos.AuthorPhotoData
import br.com.alaksion.myapplication.data.model.authorphotos.AuthorPhotoUrls
import br.com.alaksion.myapplication.data.model.authorphotos.mapToDomain

object AuthorPhotosTestData {

    val DATA_RESPONSE = listOf<AuthorPhotoData>(
        AuthorPhotoData("", urls = AuthorPhotoUrls(""))
    )

    val DOMAIN_RESPONSE = DATA_RESPONSE.map { item -> item.mapToDomain() }

}