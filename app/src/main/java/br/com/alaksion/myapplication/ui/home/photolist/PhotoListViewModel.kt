package br.com.alaksion.myapplication.ui.home.photolist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.domain.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
) : BaseViewModel() {

    private val _photos = mutableStateListOf<PhotoResponse>()
    val photos: SnapshotStateList<PhotoResponse>
        get() = _photos

    private var currentPage = 1

    private val _isScreenLoading = mutableStateOf(true)
    val isScreenLoading: State<Boolean>
        get() = _isScreenLoading

    private val _isMorePhotosLoading = mutableStateOf(false)
    val isMorePhotosLoading: State<Boolean>
        get() = _isMorePhotosLoading

    init {
        viewModelScope.launch {
            handleApiResponse(
                source = getPhotosUseCase(currentPage),
                onSuccess = { data -> handleGetPhotosSuccess(data) },
                onError = { error -> Unit }
            )
        }
    }

    private fun handleGetPhotosSuccess(data: List<PhotoResponse>?) {
        data?.let { response ->
            _photos.addAll(response)
            _isScreenLoading.value = false
        }
    }

    private fun handleGetPhotosError(error: NetworkError) {
        // If there are photos loaded show error toast
        // if there are no photos loaded show error component with try again
    }

    fun loadMorePhotos() {
        viewModelScope.launch {
            _isMorePhotosLoading.value = true
            currentPage++
            handleApiResponse(
                source = getPhotosUseCase(currentPage),
                onSuccess = { data -> handleGetMorePhotosSuccess(data) },
                onError = { error -> handleGetMorePhotosError(error) }
            )
        }
    }

    private fun handleGetMorePhotosSuccess(data: List<PhotoResponse>?) {
        data?.let { response ->
            _photos.addAll(response)
            _isMorePhotosLoading.value = false
        }
    }

    private fun handleGetMorePhotosError(error: NetworkError) {
        _isMorePhotosLoading.value = false
    }

}