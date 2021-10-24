package br.com.alaksion.myapplication.data.model.photo

import com.google.gson.annotations.SerializedName

data class PhotoLinksData(
    @SerializedName("download_location")
    val download: String?
)