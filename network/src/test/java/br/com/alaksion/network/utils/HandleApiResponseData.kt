package br.com.alaksion.network.utils

import retrofit2.Response

object HandleApiResponseData {

    val SuccessResponseContent: Response<String> = Response.success(200, "success")

}