package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.domain.usecase.GetApiKeyUseCase

abstract class UnsplashServiceCompanion<T> {

    inline fun <reified T> create(getApiKeyUseCase: GetApiKeyUseCase): T {
        return makeRetrofitClient(getApiKeyUseCase).create(T::class.java)
    }

}