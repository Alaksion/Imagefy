package br.com.alaksion.myapplication.domain.model

data class SearchPhotosRequest(
    val page: Int,
    val query: String
)