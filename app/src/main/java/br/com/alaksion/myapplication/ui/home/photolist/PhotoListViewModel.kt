package br.com.alaksion.myapplication.ui.home.photolist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.domain.model.Photo
import br.com.alaksion.myapplication.domain.usecase.GetPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
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

sealed class PhotoListEvents() {
    class ShowMorePhotosError() : PhotoListEvents()
}

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val ratePhotoUseCase: RatePhotoUseCase
) : BaseViewModel() {

    private val _events = MutableSharedFlow<PhotoListEvents>()
    val events: SharedFlow<PhotoListEvents>
        get() = _events

    private val _photos = mutableStateListOf<Photo>()
    val photos: SnapshotStateList<Photo>
        get() = _photos

    private val _screenState = MutableStateFlow<ViewState<Unit>>(ViewState.Loading())
    val screenState: StateFlow<ViewState<Unit>>
        get() = _screenState

    private val _isMorePhotosLoading = MutableStateFlow(false)
    val isMorePhotosLoading: StateFlow<Boolean>
        get() = _isMorePhotosLoading

    private val _isListRefreshing = MutableStateFlow(false)
    val isListRefreshing: StateFlow<Boolean>
        get() = _isListRefreshing

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

    private fun handleGetPhotosSuccess(data: List<Photo>?) {
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

    private fun handleLoadMorePhotosSuccess(data: List<Photo>?) {
        data?.let { response ->
            _isMorePhotosLoading.value = false
            _photos.addAll(response)
            return
        }
        showMorePhotosError()
    }

    fun ratePhoto(photo: Photo, isLike: Boolean) {
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
        produceEvent(PhotoListEvents.ShowMorePhotosError())
    }

    fun onRefreshList() {
        currentPage = 1
        _isListRefreshing.value = true
        handleApiResponse(
            source = { getPhotosUseCase(currentPage) },
            onSuccess = { data -> onRefreshSuccess(data) },
            onError = { error -> showMorePhotosError() }
        )
    }

    private fun onRefreshSuccess(data: List<Photo>?) {
        data?.let { response ->
            _isListRefreshing.value = false
            _photos.clear()
            _photos.addAll(response)
            return
        }
        showMorePhotosError()
    }

    private fun produceEvent(event: PhotoListEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }

    }

}