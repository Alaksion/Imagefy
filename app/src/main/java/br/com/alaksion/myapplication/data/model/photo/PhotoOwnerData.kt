package br.com.alaksion.myapplication.data.model.photo

import com.google.gson.annotations.SerializedName

data class PhotoOwnerData(
    val username: String,
    val name: String,
    @SerializedName("profile_image")
    val profileImage: PhotoOwnerProfileImageData
)