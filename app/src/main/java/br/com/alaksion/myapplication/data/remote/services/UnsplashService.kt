package br.com.alaksion.myapplication.data.remote.services

import br.com.alaksion.myapplication.data.model.auth.AuthValidationResponseData
import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.authorphotos.AuthorPhotoData
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.photodetails.PhotoDetailsData
import br.com.alaksion.myapplication.data.remote.UnsplashServiceCompanion
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashService {

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

    @GET("photos/{id}")
    suspend fun getPhotoDetails(@Path("id") photoId: String): Response<PhotoDetailsData>

    companion object : UnsplashServiceCompanion<UnsplashService>()

}