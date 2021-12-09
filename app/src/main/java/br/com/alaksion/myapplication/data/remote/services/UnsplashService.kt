package br.com.alaksion.myapplication.data.remote.services

import br.com.alaksion.myapplication.data.model.author.UserResponseData
import br.com.alaksion.myapplication.data.model.currentuser.CurrentUserResponseData
import br.com.alaksion.myapplication.data.model.photo.PhotoData
import br.com.alaksion.myapplication.data.model.searchphotos.SearchPhotosResponseData
import br.com.alaksion.network.client.UnsplashServiceCompanion
import retrofit2.Response
import retrofit2.http.*

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
    ): Response<List<PhotoData>>

    @GET("photos/{id}")
    suspend fun getPhotoDetails(@Path("id") photoId: String): Response<PhotoData>

    @POST("photos/{id}/like")
    suspend fun likePhoto(@Path("id") photoId: String): Response<Unit>

    @DELETE("photos/{id}/like")
    suspend fun unlikePhoto(@Path("id") photoId: String): Response<Unit>

    @GET("/me")
    suspend fun getCurrentUsername(): Response<CurrentUserResponseData>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") searchQuery: String,
        @Query("page") page: Int
    ): Response<SearchPhotosResponseData>

    companion object : UnsplashServiceCompanion<UnsplashService>()

}