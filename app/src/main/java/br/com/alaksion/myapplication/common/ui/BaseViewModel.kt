package br.com.alaksion.myapplication.common.ui

import androidx.lifecycle.ViewModel
import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.network.Source

abstract class BaseViewModel : ViewModel() {

    protected fun <T> handleApiResponse(
        source: Source<T>,
        onSuccess: (T?) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        if (source is Source.Error) onError(source.errorData)
        else onSuccess((source as Source.Success).data)
    }
}