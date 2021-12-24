package br.com.alaksion.myapplication.domain.model

data class SearchPhotos(
    val photos: List<Photo>,
    val totalPages: Int
)
