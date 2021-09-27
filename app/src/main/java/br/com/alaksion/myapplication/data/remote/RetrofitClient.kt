package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.domain.usecase.GetApiKeyUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun makeRetrofitClient(
    getApiKeyUseCase: GetApiKeyUseCase
) = createRetrofitInstance(buildHttpClient(getApiKeyUseCase))

private fun createRetrofitInstance(httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(AppRemoteConfig.baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun buildHttpClient(
    getApiKeyUseCase: GetApiKeyUseCase
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .addHeader(AppRemoteConfig.AUTH_HEADER, getApiKeyUseCase())
                    .build()
            chain.proceed(request)
        }
        .build()
}