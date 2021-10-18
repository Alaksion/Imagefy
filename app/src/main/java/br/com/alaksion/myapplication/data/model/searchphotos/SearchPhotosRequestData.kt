package br.com.alaksion.myapplication.data.model.searchphotos

import br.com.alaksion.myapplication.domain.model.SearchPhotosRequest

data class SearchPhotosRequestData(
    val page: Int,
    val query: String
)

fun SearchPhotosRequest.mapToData() = SearchPhotosRequestData(
    page = this.page,
    query = this.query
)