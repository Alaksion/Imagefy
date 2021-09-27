package br.com.alaksion.myapplication.common.network

import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <Res : Any> Response<Res>.handleApiResponse(): Source<Res> {
    return try {
        handleSuccess()
    } catch (exception: Exception) {
        handleException(exception)
    }
}

private fun <Res : Any> Response<Res>.handleSuccess(
): Source<Res> {
    return if (isSuccessful) {
        val body = body()
        body?.let {
            Source.Success(it, code())
        } ?: when (code()) {
            204 -> Source.Success(null, code())
            else -> throw NullPointerException("response body is null")
        }
    } else throw HttpException(this)
}

private fun <T> handleException(e: Exception): Source.Error<T> {
    val error = when (e) {
        is ConnectException, is UnknownHostException -> NetworkError(
            code = 500,
            message = "An unexpected error occurend"
        )
        is HttpException -> NetworkError(code = e.code(), message = e.message())
        is SocketTimeoutException -> NetworkError(code = 504, message = "Socket timed out")
        else -> NetworkError(code = 500, message = "An unexpected error occurred")
    }
    return Source.Error(error)
}