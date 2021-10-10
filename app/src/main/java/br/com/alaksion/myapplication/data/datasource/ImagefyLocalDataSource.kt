package br.com.alaksion.myapplication.data.datasource

interface ImagefyLocalDataSource {

    fun storeAuthorizationHeader(value: String)

    fun clearAuthorizationHeader()

}