package br.com.alaksion.network.client

import br.com.alaksion.network.client.domain.usecase.GetAuthorizationHeaderUseCase

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