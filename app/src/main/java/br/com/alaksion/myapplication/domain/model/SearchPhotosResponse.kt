package br.com.alaksion.myapplication.domain.model

data class SearchPhotosResponse(
    val photos: List<PhotoResponse>,
    val totalPages: Int
)
