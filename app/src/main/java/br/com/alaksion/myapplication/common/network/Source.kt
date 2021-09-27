package br.com.alaksion.myapplication.common.network

sealed class Source<out T>(data: T? = null) {

    class Success<T>(val data: T?, val resultCode: Int? = null) : Source<T>(data)
    class Error<T>(val errorData: NetworkError) : Source<T>(null)
    class Loading<T>() : Source<T>(null)

}

fun <T, Domain> Source<T>.mapSource(mapper: (T?) -> Domain?): Source<Domain> {
    if (this is Source.Error) return Source.Error(this.errorData)
    if (this is Source.Loading) return Source.Loading()

    return Source.Success(mapper((this as Source.Success).data))

}