package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.author.UserResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos")
    suspend fun getPhotos(@Query("page") page: Int): Response<List<PhotoData>>

    @GET("users/{username}")
    suspend fun getAuthorProfile(@Path("username") username: String): Response<UserResponseData>

    companion object : UnsplashServiceCompanion<UnsplashApi>()

}