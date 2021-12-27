package br.com.alaksion.myapplication.data.remote.services

import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.currentuser.CurrentUserResponseData
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.searchphotos.SearchPhotosResponseData
import br.com.alaksion.network.client.UnsplashServiceCompanion
import retrofit2.http.*

interface UnsplashService {

    @GET("photos")
    suspend fun getPhotos(@Query("page") page: Int): List<PhotoData>

    @GET("users/{username}")
    suspend fun getAuthorProfile(@Path("username") username: String): UserResponseData

    @GET("users/{username}/photos")
    suspend fun getAuthorPhotos(
        @Path("username") username: String,
        @Query("per_page") resultsPerPage: Int = 12,
        @Query("page") page: Int
    ): List<PhotoData>

    @GET("photos/{id}")
    suspend fun getPhotoDetails(@Path("id") photoId: String): PhotoData

    @POST("photos/{id}/like")
    suspend fun likePhoto(@Path("id") photoId: String): Unit

    @DELETE("photos/{id}/like")
    suspend fun unlikePhoto(@Path("id") photoId: String): Unit

    @GET("/me")
    suspend fun getCurrentUsername(): CurrentUserResponseData

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") searchQuery: String,
        @Query("page") page: Int
    ): SearchPhotosResponseData

    companion object : UnsplashServiceCompanion<UnsplashService>()

}