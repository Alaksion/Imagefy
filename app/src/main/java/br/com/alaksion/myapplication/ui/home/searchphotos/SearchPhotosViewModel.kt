package br.com.alaksion.myapplication.ui.home.searchphotos

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.domain.model.Photo
import br.com.alaksion.myapplication.domain.model.SearchPhotos
import br.com.alaksion.myapplication.domain.usecase.SearchPhotosUseCase
import br.com.alaksion.myapplication.ui.model.BaseViewModel
import br.com.alaksion.myapplication.ui.model.ViewState
import br.com.alaksion.network.model.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SearchPhotosEvents() {
    class MorePhotosError() : SearchPhotosEvents()
}

@HiltViewModel
class SearchPhotosViewModel @Inject constructor(
    private val searchPhotosUseCase: SearchPhotosUseCase
) : BaseViewModel() {

    private val _events = MutableSharedFlow<SearchPhotosEvents>()
    val events: SharedFlow<SearchPhotosEvents>
        get() = _events

    private val _screenState: MutableStateFlow<ViewState<Unit>> = MutableStateFlow(ViewState.Idle())
    val screenState: StateFlow<ViewState<Unit>>
        get() = _screenState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String>
        get() = _searchQuery

    private val _isMorePhotosLoading = MutableStateFlow(false)
    val isMorePhotosLoading: StateFlow<Boolean>
        get() = _isMorePhotosLoading

    private val _photoList = mutableStateListOf<Photo>()
    val photoList: SnapshotStateList<Photo>
        get() = _photoList

    private var currentPage = 1
    private var maxPages = 1

    private fun getPhotos(
        onSuccess: (data: SearchPhotos?) -> Unit,
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

    private fun onSearchPhotosSuccess(data: SearchPhotos?) {
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

    private fun onLoadMorePhotosSuccess(data: SearchPhotos?) {
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
        produceEvent(SearchPhotosEvents.MorePhotosError())
    }


    private fun produceEvent(event: SearchPhotosEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }

    }

}