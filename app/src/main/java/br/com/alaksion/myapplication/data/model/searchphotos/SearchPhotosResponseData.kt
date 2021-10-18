package br.com.alaksion.myapplication.data.model.searchphotos

import br.com.alaksion.myapplication.data.model.photo.PhotoData

data class SearchPhotosResponseData(
    val results: List<PhotoData>
)
