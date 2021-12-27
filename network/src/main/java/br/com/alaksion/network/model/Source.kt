package br.com.alaksion.network.model

sealed class Source<out T> {

    class Success<T>(val data: T?, val resultCode: Int? = null) : Source<T>()
    class Error<T>(val errorData: NetworkError) : Source<T>()
    class Loading<T> : Source<T>()

    fun getValue(
        onSuccess: (T?) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        if (this is Success) onSuccess(this.data)
        else onError((this as Error).errorData)
    }

    fun <Domain> mapSource(mapper: (T?) -> Domain?): Source<Domain> {
        if (this is Error) return Error(this.errorData)
        if (this is Loading) return Loading()

        return Success(mapper((this as Success).data))
    }

}

