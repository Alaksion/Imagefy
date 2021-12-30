package br.com.alaksion.myapplication.ui.home.photoviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.alaksion.myapplication.domain.model.PhotoDetail
import br.com.alaksion.myapplication.domain.usecase.GetPhotoDetailsUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.myapplication.ui.model.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PhotoViewerViewModel @AssistedInject constructor(
    @Assisted private val photoId: String,
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase,
    private val ratePhotoUseCase: RatePhotoUseCase
) : BaseViewModel() {

    private val _photoData: MutableStateFlow<PhotoViewerState> =
        MutableStateFlow(PhotoViewerState.Loading)
    val photoData: StateFlow<PhotoViewerState>
        get() = _photoData

    init {
        getPhotoDetails()
    }

    fun getPhotoDetails() {
        _photoData.value = PhotoViewerState.Loading
        handleApiResponse(
            source = { getPhotoDetailsUseCase(photoId) },
            onSuccess = { data -> onGetPhotoDetailsSuccess(data) },
            onError = { onGetPhotoDetailsError() }
        )
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

    @AssistedFactory
    interface Factory {
        fun create(photoId: String): PhotoViewerViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            factory: Factory,
            photoId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return factory.create(photoId) as T
            }
        }
    }
}