package br.com.alaksion.myapplication.ui.home.photoviewer

import br.com.alaksion.myapplication.domain.model.PhotoDetail
import br.com.alaksion.myapplication.domain.usecase.GetPhotoDetailsUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.myapplication.ui.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PhotoViewerViewModel @Inject constructor(
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase,
    private val ratePhotoUseCase: RatePhotoUseCase
) : BaseViewModel() {

    private val _photoData: MutableStateFlow<PhotoViewerState> =
        MutableStateFlow(PhotoViewerState.Loading)
    val photoData: StateFlow<PhotoViewerState>
        get() = _photoData

    private val photoId: String = ""

    fun getPhotoDetails(currentPhotoId: String) {
        if (photoId != currentPhotoId) {
            _photoData.value = PhotoViewerState.Loading
            handleApiResponse(
                source = { getPhotoDetailsUseCase(currentPhotoId) },
                onSuccess = { data -> onGetPhotoDetailsSuccess(data) },
                onError = { onGetPhotoDetailsError() }
            )
        }
    }

    private fun onGetPhotoDetailsError() {
        _photoData.value = PhotoViewerState.Error
    }

    private fun onGetPhotoDetailsSuccess(data: PhotoDetail?) {
        data?.let { response ->
            _photoData.value = PhotoViewerState.Ready(response)
            return
        }
        _photoData.value = PhotoViewerState.Error
    }

    fun ratePhoto(photoId: String, isLike: Boolean) {

        handleApiResponse(
            source = { ratePhotoUseCase(isLike, photoId) },
            onSuccess = { },
            onError = { }
        )

    }
}