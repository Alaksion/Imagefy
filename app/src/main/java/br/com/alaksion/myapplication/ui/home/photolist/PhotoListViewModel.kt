package br.com.alaksion.myapplication.ui.home.photolist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.utils.Event
import br.com.alaksion.myapplication.domain.model.PhotoResponse
import br.com.alaksion.myapplication.domain.usecase.GetPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.network.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val ratePhotoUseCase: RatePhotoUseCase
) : BaseViewModel() {

    private val _photos = mutableStateListOf<PhotoResponse>()
    val photos: SnapshotStateList<PhotoResponse>
        get() = _photos

    private val _screenState = mutableStateOf<ViewState<Unit>>(ViewState.Loading())
    val screenState: State<ViewState<Unit>>
        get() = _screenState

    private val _isMorePhotosLoading = mutableStateOf(false)
    val isMorePhotosLoading: State<Boolean>
        get() = _isMorePhotosLoading

    private val _showLoadMorePhotosError = MutableLiveData<Event<Unit>>()
    val showMorePhotosError: LiveData<Event<Unit>>
        get() = _showLoadMorePhotosError

    private var currentPage = 1

    init {
        getImages()
    }

    fun getImages() {
        _screenState.value = ViewState.Loading()
        viewModelScope.launch {
            getPhotosUseCase(currentPage).collect {
                handleApiResponse(
                    source = it,
                    onSuccess = { data -> handleGetPhotosSuccess(data) },
                    onError = { error -> handleGetPhotosError(error) }
                )
            }
        }
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
        viewModelScope.launch {
            _isMorePhotosLoading.value = true
            currentPage++
            getPhotosUseCase(currentPage).collect {
                handleApiResponse(
                    source = it,
                    onSuccess = { data -> handleLoadMorePhotosSuccess(data) },
                    onError = { onLoadMorePhotosError() }
                )
            }
        }
    }

    private fun onLoadMorePhotosError() {
        _showLoadMorePhotosError.postValue(Event(Unit))
        _isMorePhotosLoading.value = false
    }

    private fun handleLoadMorePhotosSuccess(data: List<PhotoResponse>?) {
        data?.let { response ->
            _isMorePhotosLoading.value = false
            _photos.addAll(response)
            return
        }
        _showLoadMorePhotosError.postValue(Event(Unit))
    }

    fun ratePhoto(photo: PhotoResponse, isLike: Boolean) {
        viewModelScope.launch {
            ratePhotoUseCase(photoId = photo.id, isLike = isLike).collect {
                handleApiResponse(
                    source = it,
                    onSuccess = {
                        photo.likedByUser = isLike
                        if (isLike) photo.likes++ else photo.likes--
                    },
                    onError = {}
                )
            }
        }
    }

}