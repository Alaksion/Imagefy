package br.com.alaksion.myapplication.ui.model

import br.com.alaksion.network.NetworkError

sealed class ViewState<out T> {

    data class Ready<T>(val data: T) : ViewState<T>()
    class Loading<T> : ViewState<T>()
    class Idle<T> : ViewState<T>()
    data class Error<T>(val error: NetworkError? = null) : ViewState<T>()

}