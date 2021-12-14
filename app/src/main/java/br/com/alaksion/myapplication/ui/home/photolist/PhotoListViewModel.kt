package br.com.alaksion.myapplication.ui.home.photolist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.ui.EventViewModel
import br.com.alaksion.myapplication.common.ui.ViewModelEvent
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.domain.usecase.GetPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.network.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PhotoListEvents() : ViewModelEvent {
    class ShowMorePhotosError() : PhotoListEvents()
}

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val ratePhotoUseCase: RatePhotoUseCase
) : EventViewModel<PhotoListEvents>() {

    private val _photos = mutableStateListOf<PhotoResponse>()
    val photos: SnapshotStateList<PhotoResponse>
        get() = _photos

    private val _screenState = MutableStateFlow<ViewState<Unit>>(ViewState.Loading())
    val screenState: StateFlow<ViewState<Unit>>
        get() = _screenState

    private val _isMorePhotosLoading = MutableStateFlow(false)
    val isMorePhotosLoading: StateFlow<Boolean>
        get() = _isMorePhotosLoading

    private var currentPage = 1

    init {
        getImages()
    }

    fun getImages() {
        _screenState.value = ViewState.Loading()
        handleApiResponse(
            source = { getPhotosUseCase(currentPage) },
            onSuccess = { data -> handleGetPhotosSuccess(data) },
            onError = { error -> handleGetPhotosError(error) }
        )
    }


    private fun handleGetPhotosSuccess(data: List<PhotoResponse>?) {
        data?.let { response ->
            _photos.addAll(response)
            _screenState.value = ViewState.Ready(Unit)
            return
        }
        _screenState.value = ViewState.Error()
    }

    private fun handleGetPhotosError(error: NetworkError) {
        _screenState.value = ViewState.Error(error)
    }

    fun loadMorePhotos() {
        _isMorePhotosLoading.value = true
        currentPage++
        handleApiResponse(
            source = { getPhotosUseCase(currentPage) },
            onSuccess = { data -> handleLoadMorePhotosSuccess(data) },
            onError = { onLoadMorePhotosError() }
        )
    }

    private fun onLoadMorePhotosError() {
        showMorePhotosError()
        _isMorePhotosLoading.value = false
    }

    private fun handleLoadMorePhotosSuccess(data: List<PhotoResponse>?) {
        data?.let { response ->
            _isMorePhotosLoading.value = false
            _photos.addAll(response)
            return
        }
        showMorePhotosError()
    }

    fun ratePhoto(photo: PhotoResponse, isLike: Boolean) {
        handleApiResponse(
            source = { ratePhotoUseCase(photoId = photo.id, isLike = isLike) },
            onSuccess = {
                photo.likedByUser = isLike
                if (isLike) photo.likes++ else photo.likes--
            },
            onError = {}
        )
    }

    private fun showMorePhotosError() {
        viewModelScope.launch {
            sendEvent(PhotoListEvents.ShowMorePhotosError())
        }
    }
}