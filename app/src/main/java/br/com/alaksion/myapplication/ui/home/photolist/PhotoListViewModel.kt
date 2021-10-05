package br.com.alaksion.myapplication.ui.home.photolist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.ui.ViewState
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

    private val _screenState = mutableStateOf<ViewState<Unit>>(ViewState.Loading())
    val screenState: State<ViewState<Unit>>
        get() = _screenState

    private var currentPage = 1

    init {
        getImages()
    }

    fun getImages() {
        viewModelScope.launch {
            handleApiResponse(
                source = getPhotosUseCase(currentPage),
                onSuccess = { data -> handleGetPhotosSuccess(data) },
                onError = { error -> handleGetPhotosError(error) }
            )
        }
    }

    private fun handleGetPhotosSuccess(data: List<PhotoResponse>?) {
        data?.let { response ->
            _photos.addAll(response)
            _screenState.value = ViewState.Ready(Unit)
        }
    }

    private fun handleGetPhotosError(error: NetworkError) {
        _screenState.value = ViewState.Error(error)
    }

    fun loadMorePhotos() {
        viewModelScope.launch {
            _screenState.value = ViewState.Loading()
            currentPage++
            handleApiResponse(
                source = getPhotosUseCase(currentPage),
                onSuccess = { data -> handleGetPhotosSuccess(data) },
                onError = { error -> handleGetPhotosError(error) }
            )
        }
    }

}