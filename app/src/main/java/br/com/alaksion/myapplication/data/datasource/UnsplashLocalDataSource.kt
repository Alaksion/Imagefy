package br.com.alaksion.myapplication.data.datasource

interface UnsplashLocalDataSource {

    fun storeAuthorizationHeader(value: String)

    fun clearAuthorizationHeader()

}