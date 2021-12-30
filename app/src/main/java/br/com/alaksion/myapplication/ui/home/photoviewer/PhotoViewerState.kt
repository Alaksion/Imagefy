package br.com.alaksion.myapplication.ui.home.photoviewer

import br.com.alaksion.myapplication.domain.model.PhotoDetail

sealed class PhotoViewerState {
    object Loading : PhotoViewerState()
    object Error : PhotoViewerState()
    data class Ready(val photoData: PhotoDetail) : PhotoViewerState()
}