package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.domain.usecase.GetAuthorizationHeaderUseCase

abstract class UnsplashServiceCompanion<T> {

    inline fun <reified T> create(
        getAuthTokenUseCase: GetAuthorizationHeaderUseCase? = null,
        baseUrl: String
    ): T {
        return makeRetrofitClient(
            getAuthTokenUseCase,
            baseUrl
        ).create(T::class.java)
    }

}