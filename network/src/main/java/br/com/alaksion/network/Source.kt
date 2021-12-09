package br.com.alaksion.network

sealed class Source<out T> {

    class Success<T>(val data: T?, val resultCode: Int? = null) : Source<T>()
    class Error<T>(val errorData: NetworkError) : Source<T>()
    class Loading<T> : Source<T>()

}

fun <T, Domain> Source<T>.mapSource(mapper: (T?) -> Domain?): Source<Domain> {
    if (this is Source.Error) return Source.Error(this.errorData)
    if (this is Source.Loading) return Source.Loading()

    return Source.Success(mapper((this as Source.Success).data))

}