package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.domain.usecase.GetClientIdUseCase

abstract class UnsplashServiceCompanion<T> {

    inline fun <reified T> create(getClientIdUseCase: GetClientIdUseCase? = null, baseUrl: String): T {
        return makeRetrofitClient(getClientIdUseCase, baseUrl).create(T::class.java)
    }

}