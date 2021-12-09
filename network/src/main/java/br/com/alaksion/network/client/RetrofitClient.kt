package br.com.alaksion.network.client

import br.com.alaksion.network.client.domain.usecase.GetAuthorizationHeaderUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun makeRetrofitClient(
    getAuthorizationHeaderUseCase: GetAuthorizationHeaderUseCase?,
    baseUrl: String
) = createRetrofitInstance(buildHttpClient(getAuthorizationHeaderUseCase), baseUrl)

private fun createRetrofitInstance(httpClient: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun buildHttpClient(
    getAuthorizationHeaderUseCase: GetAuthorizationHeaderUseCase?
): OkHttpClient {

    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .addHeader(
                        NetworkClientConfig.AUTH_HEADER,
                        getAuthorizationHeaderUseCase?.let { "Bearer ${it()}" } ?: "")
                    .build()
            chain.proceed(request)
        }
        .build()
}