package br.com.alaksion.myapplication.ui.home.photoviewer

import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.PhotoDetailResponse
import br.com.alaksion.myapplication.domain.usecase.GetPhotoDetailsUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.network.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PhotoViewerViewModel @Inject constructor(
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase,
    private val ratePhotoUseCase: RatePhotoUseCase
) : BaseViewModel() {

    private val _photoData: MutableStateFlow<ViewState<PhotoDetailResponse>> =
        MutableStateFlow(ViewState.Loading())
    val photoData: StateFlow<ViewState<PhotoDetailResponse>>
        get() = _photoData

    private val photoId: String = ""

    fun getPhotoDetails(currentPhotoId: String) {
        if (photoId != currentPhotoId) {
            _photoData.value = ViewState.Loading()
            handleApiResponse(
                source = { getPhotoDetailsUseCase(currentPhotoId) },
                onSuccess = { data -> onGetPhotoDetailsSuccess(data) },
                onError = { error -> onGetPhotoDetailsError(error) }
            )
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

        handleApiResponse(
            source = { ratePhotoUseCase(isLike, photoId) },
            onSuccess = { },
            onError = { }
        )

    }
}