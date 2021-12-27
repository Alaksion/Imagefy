package br.com.alaksion.network.client

import br.com.alaksion.network.client.domain.usecase.GetAuthorizationHeaderUseCase
import br.com.alaksion.network.model.NetworkError
import br.com.alaksion.network.model.Source
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    fun makeClient(
        getAuthorizationHeaderUseCase: GetAuthorizationHeaderUseCase?,
        baseUrl: String
    ) = createRetrofitInstance(makeHttpClient(getAuthorizationHeaderUseCase), baseUrl)

    private fun createRetrofitInstance(httpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun makeHttpClient(
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

    suspend fun <T> makeApiCall(request: suspend () -> T): Source<T> {
        return try {
            Source.Success(request())
        } catch (e: Exception) {
            Source.Error(
                if (e is HttpException) {
                    NetworkError(message = e.message(), code = e.code())
                } else {
                    NetworkError(message = "An unexpected error occurred", code = 500)
                }
            )
        }
    }

}
