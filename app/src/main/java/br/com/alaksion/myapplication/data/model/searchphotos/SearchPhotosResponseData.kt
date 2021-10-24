package br.com.alaksion.myapplication.data.model.searchphotos

import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.photo.mapToPhotoResponse
import br.com.alaksion.myapplication.domain.model.SearchPhotosResponse
import com.google.gson.annotations.SerializedName

data class SearchPhotosResponseData(
    val results: List<PhotoData>,
    @SerializedName("total_pages")
    val totalPages: Int
)

fun SearchPhotosResponseData.mapToSearchPhotosResponse() = SearchPhotosResponse(
    totalPages = this.totalPages,
    photos = this.results.map { item -> item.mapToPhotoResponse() }
)
