package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.authorphotos.AuthorPhotoData
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos")
    suspend fun getPhotos(@Query("page") page: Int): Response<List<PhotoData>>

    @GET("users/{username}")
    suspend fun getAuthorProfile(@Path("username") username: String): Response<UserResponseData>

    @GET("users/{username}/photos")
    suspend fun getAuthorPhotos(
        @Path("username") username: String,
        @Query("per_page") resultsPerPage: Int = 12,
        @Query("page") page: Int
    ): Response<List<AuthorPhotoData>>

    companion object : UnsplashServiceCompanion<UnsplashApi>()

}