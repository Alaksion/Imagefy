package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.domain.usecase.GetClientIdUseCase

abstract class UnsplashServiceCompanion<T> {

    inline fun <reified T> create(getClientIdUseCase: GetClientIdUseCase): T {
        return makeRetrofitClient(getClientIdUseCase).create(T::class.java)
    }

}