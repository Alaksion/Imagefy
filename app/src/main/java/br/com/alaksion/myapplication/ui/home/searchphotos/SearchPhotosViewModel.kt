package br.com.alaksion.myapplication.ui.home.searchphotos

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.domain.model.SearchPhotosResponse
import br.com.alaksion.myapplication.domain.usecase.SearchPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPhotosViewModel @Inject constructor(
    private val searchPhotosUseCase: SearchPhotosUseCase
) : BaseViewModel() {

    private val _screenState = mutableStateOf<ViewState<Unit>>(ViewState.Idle())
    val screenState: State<ViewState<Unit>>
        get() = _screenState

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String>
        get() = _searchQuery

    private val _photoList = mutableStateListOf<PhotoResponse>()
    val photoList: SnapshotStateList<PhotoResponse>
        get() = _photoList

    private val _isMorePhotosLoading = mutableStateOf(false)
    val isMorePhotosLoading: State<Boolean>
        get() = _isMorePhotosLoading

    private var currentPage = 1
    private var maxPages = 1

    private fun getPhotos(
        onSuccess: (data: SearchPhotosResponse?) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        viewModelScope.launch {
            handleApiResponse(
                source = searchPhotosUseCase(page = currentPage, searchQuery = searchQuery.value),
                onError = { error -> onError(error) },
                onSuccess = { data -> onSuccess(data) }
            )
        }
    }

    fun onChangeSearchQuery(value: String) {
        _searchQuery.value = value
    }

    fun searchPhotos() {
        _screenState.value = ViewState.Loading()
        currentPage = 1
        _photoList.clear()
        getPhotos(
            onSuccess = { data -> onSearchPhotosSuccess(data) },
            onError = { onErrorSearchPhotos() }
        )
    }

    private fun onSearchPhotosSuccess(data: SearchPhotosResponse?) {
        data?.let { response ->
            _screenState.value = ViewState.Ready(Unit)
            _photoList.addAll(response.photos)
            maxPages = response.totalPages
        }
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
        }

    }

    private fun onErrorLoadMorePhotos() {
        _isMorePhotosLoading.value = false
    }

}