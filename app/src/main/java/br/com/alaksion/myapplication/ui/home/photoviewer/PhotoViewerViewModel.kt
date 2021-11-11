package br.com.alaksion.myapplication.ui.home.photoviewer

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.domain.usecase.GetPhotoDetailsUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewerViewModel @Inject constructor(
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase,
    private val ratePhotoUseCase: RatePhotoUseCase
) : BaseViewModel() {

    private val _photoData = mutableStateOf<ViewState<PhotoDetailResponse>>(ViewState.Loading())
    val photoData: State<ViewState<PhotoDetailResponse>>
        get() = _photoData

    private val photoId: String = ""

    fun getPhotoDetails(currentPhotoId: String) {
        if (photoId != currentPhotoId) {
            _photoData.value = ViewState.Loading()
            viewModelScope.launch {
                getPhotoDetailsUseCase(currentPhotoId).collect {
                    handleApiResponse(
                        source = it,
                        onSuccess = { data -> onGetPhotoDetailsSuccess(data) },
                        onError = { error -> onGetPhotoDetailsError(error) }
                    )
                }
            }
        }
    }

    private fun onGetPhotoDetailsError(error: NetworkError) {
        _photoData.value = ViewState.Error(error)
    }

    private fun onGetPhotoDetailsSuccess(data: PhotoDetailResponse?) {
        data?.let { response ->
            _photoData.value = ViewState.Ready(response)
            return
        }
        _photoData.value = ViewState.Error()
    }

    fun ratePhoto(photoId: String, isLike: Boolean) {
        viewModelScope.launch {
            ratePhotoUseCase(isLike, photoId).collect {
                handleApiResponse(
                    source = it,
                    onSuccess = { },
                    onError = { }
                )
            }
        }
    }

}