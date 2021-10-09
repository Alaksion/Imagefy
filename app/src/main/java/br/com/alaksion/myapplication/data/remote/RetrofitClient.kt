package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.domain.usecase.GetClientIdUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun makeRetrofitClient(
    getClientIdUseCase: GetClientIdUseCase
) = createRetrofitInstance(buildHttpClient(getClientIdUseCase))

private fun createRetrofitInstance(httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(AppRemoteConfig.baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun buildHttpClient(
    getClientIdUseCase: GetClientIdUseCase
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .addHeader(AppRemoteConfig.AUTH_HEADER, getClientIdUseCase())
                    .build()
            chain.proceed(request)
        }
        .build()
}