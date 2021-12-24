package br.com.alaksion.myapplication.ui.home.authordetails

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.ui.model.EventViewModel
import br.com.alaksion.myapplication.ui.model.ViewModelEvent
import br.com.alaksion.myapplication.ui.model.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotos
import br.com.alaksion.myapplication.domain.model.Author
import br.com.alaksion.myapplication.domain.usecase.GetAuthorPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.network.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthorDetailsEvents() : ViewModelEvent {
    class ShowErrorToast() : AuthorDetailsEvents()
}

@HiltViewModel
class AuthorDetailsViewModel @Inject constructor(
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase,
    private val getAuthorPhotosUseCase: GetAuthorPhotosUseCase
) : EventViewModel<AuthorDetailsEvents>() {

    private val _authorData: MutableStateFlow<ViewState<Author>> =
        MutableStateFlow(ViewState.Loading())
    val authorData: StateFlow<ViewState<Author>>
        get() = _authorData

    private val _authorPhotosState: MutableStateFlow<ViewState<Unit>> =
        MutableStateFlow(ViewState.Loading())
    val authorPhotosState: StateFlow<ViewState<Unit>>
        get() = _authorPhotosState

    private val _authorPhotos = mutableStateListOf<AuthorPhotos>()
    val authorPhotos: SnapshotStateList<AuthorPhotos>
        get() = _authorPhotos

    private var authorUsername: String = ""
    private var page = 1

    fun getAuthorProfileData(currentAuthorUsername: String) {
        this.authorUsername = currentAuthorUsername
        handleApiResponse(
            source = { getAuthorProfileUseCase(currentAuthorUsername) },
            onError = { error -> onGetAuthorDataError(error) },
            onSuccess = { data -> onGetAuthorDataSuccess(data) }
        )
    }

    private fun onGetAuthorDataSuccess(data: Author?) {
        data?.let { response ->
            _authorData.value = ViewState.Ready(response)
            getAuthorPhotos()
            return
        }
        _authorData.value = ViewState.Error()
    }

    private fun onGetAuthorDataError(error: NetworkError) {
        _authorData.value = ViewState.Error(error)
    }

    private fun getAuthorPhotos() {
        handleApiResponse(
            source = {
                getAuthorPhotosUseCase(
                    username = authorUsername,
                    page = page
                )
            },
            onSuccess = { data -> onGetAuthorPhotosSuccess(data) },
            onError = { error -> onGetAuthorPhotosError(error) }
        )
    }

    private fun onGetAuthorPhotosSuccess(data: List<AuthorPhotos>?) {
        data?.let { response ->
            _authorPhotos.addAll(response)
            _authorPhotosState.value = ViewState.Ready(Unit)
            return
        }
        _authorPhotosState.value = ViewState.Error()
    }

    private fun onGetAuthorPhotosError(error: NetworkError) {
        _authorPhotosState.value = ViewState.Error(error)
    }

    fun getMoreAuthorPhotos() {
        handleApiResponse(
            source = {
                getAuthorPhotosUseCase(
                    username = authorUsername,
                    page = page
                )
            },
            onSuccess = { data -> onGetMorePhotosSuccess(data) },
            onError = { onGetMorePhotosError() }
        )
    }

    private fun onGetMorePhotosSuccess(data: List<AuthorPhotos>?) {
        data?.let { response ->
            _authorPhotos.addAll(response)
            return
        }
        showErrorToast()
    }

    private fun onGetMorePhotosError() {
        showErrorToast()
    }

    private fun showErrorToast() {
        viewModelScope.launch {
            sendEvent(AuthorDetailsEvents.ShowErrorToast())
        }
    }

}