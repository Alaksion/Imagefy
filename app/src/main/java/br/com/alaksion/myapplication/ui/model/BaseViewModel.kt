package br.com.alaksion.myapplication.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alaksion.network.NetworkError
import br.com.alaksion.network.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected open fun <T> handleApiResponse(
        source: suspend () -> Flow<Source<T>>,
        onSuccess: (T?) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        viewModelScope.launch {
            source().collect { response ->
                if (response is Source.Error) onError(response.errorData)
                else onSuccess((response as Source.Success).data)
            }
        }
    }
}