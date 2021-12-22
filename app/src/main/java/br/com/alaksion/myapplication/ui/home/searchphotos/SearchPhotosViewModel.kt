package br.com.alaksion.myapplication.ui.home.searchphotos

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.ui.model.EventViewModel
import br.com.alaksion.myapplication.ui.model.ViewModelEvent
import br.com.alaksion.myapplication.ui.model.ViewState
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.domain.model.SearchPhotosResponse
import br.com.alaksion.myapplication.domain.usecase.SearchPhotosUseCase
import br.com.alaksion.network.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SearchPhotosEvents() : ViewModelEvent {
    class MorePhotosError() : SearchPhotosEvents()
}

@HiltViewModel
class SearchPhotosViewModel @Inject constructor(
    private val searchPhotosUseCase: SearchPhotosUseCase
) : EventViewModel<SearchPhotosEvents>() {

    private val _screenState: MutableStateFlow<ViewState<Unit>> = MutableStateFlow(ViewState.Idle())
    val screenState: StateFlow<ViewState<Unit>>
        get() = _screenState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String>
        get() = _searchQuery

    private val _isMorePhotosLoading = MutableStateFlow(false)
    val isMorePhotosLoading: StateFlow<Boolean>
        get() = _isMorePhotosLoading

    private val _photoList = mutableStateListOf<PhotoResponse>()
    val photoList: SnapshotStateList<PhotoResponse>
        get() = _photoList

    private var currentPage = 1
    private var maxPages = 1

    private fun getPhotos(
        onSuccess: (data: SearchPhotosResponse?) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        handleApiResponse(
            source = {
                searchPhotosUseCase(
                    page = currentPage,
                    searchQuery = searchQuery.value
                )
            },
            onError = { error -> onError(error) },
            onSuccess = { data -> onSuccess(data) }
        )
    }

    fun onChangeSearchQuery(value: String) {
        _searchQuery.value = value
    }

    fun searchPhotos() {
        _screenState.value = ViewState.Loading()
        currentPage = 1
        getPhotos(
            onSuccess = { data -> onSearchPhotosSuccess(data) },
            onError = { onErrorSearchPhotos() }
        )
    }

    private fun onSearchPhotosSuccess(data: SearchPhotosResponse?) {
        data?.let { response ->
            _photoList.clear()
            _photoList.addAll(response.photos)
            _screenState.value = ViewState.Ready(Unit)
            maxPages = response.totalPages
            return
        }
        _screenState.value = ViewState.Error()
    }

    private fun onErrorSearchPhotos() {
        _screenState.value = ViewState.Error()
    }

    fun loadMorePhotos() {
        if (currentPage < maxPages) {
            _isMorePhotosLoading.value = true
            currentPage++
            getPhotos(
                onSuccess = { data -> onLoadMorePhotosSuccess(data) },
                onError = { onErrorLoadMorePhotos() }
            )
        }
    }

    private fun onLoadMorePhotosSuccess(data: SearchPhotosResponse?) {
        data?.let { response ->
            _isMorePhotosLoading.value = false
            _photoList.addAll(response.photos)
            return
        }
        showMorePhotosError()
    }

    private fun onErrorLoadMorePhotos() {
        _isMorePhotosLoading.value = false
        showMorePhotosError()
    }

    private fun showMorePhotosError() {
        viewModelScope.launch {
            sendEvent(SearchPhotosEvents.MorePhotosError())
        }
    }

}