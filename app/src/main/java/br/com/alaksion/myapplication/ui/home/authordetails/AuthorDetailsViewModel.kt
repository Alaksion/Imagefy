package br.com.alaksion.myapplication.ui.home.authordetails

import androidx.compose.runtime.MutableState
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
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.usecase.GetAuthorPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.network.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorDetailsViewModel @Inject constructor(
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase,
    private val getAuthorPhotosUseCase: GetAuthorPhotosUseCase
) : BaseViewModel() {

    private val _authorData: MutableState<ViewState<AuthorResponse>> =
        mutableStateOf(ViewState.Loading())
    val authorData: State<ViewState<AuthorResponse>>
        get() = _authorData

    private val _authorPhotosState: MutableState<ViewState<Unit>> =
        mutableStateOf(ViewState.Loading())
    val authorPhotosState: State<ViewState<Unit>>
        get() = _authorPhotosState

    private val _authorPhotos = mutableStateListOf<AuthorPhotosResponse>()
    val authorPhotos: SnapshotStateList<AuthorPhotosResponse>
        get() = _authorPhotos

    private val _showErrorToast = MutableLiveData<Event<Unit>>()
    val showErrorToast: LiveData<Event<Unit>>
        get() = _showErrorToast

    private var authorUsername: String = ""
    private var page = 1

    fun getAuthorProfileData(currentAuthorUsername: String) {
        if (currentAuthorUsername != this.authorUsername) {
            this.authorUsername = currentAuthorUsername

            viewModelScope.launch {
                _authorData.value = ViewState.Loading()
                getAuthorProfileUseCase(currentAuthorUsername).collect {
                    handleApiResponse(
                        source = it,
                        onError = { error -> onGetAuthorDataError(error) },
                        onSuccess = { data -> onGetAuthorDataSuccess(data) }
                    )
                }
            }
        }
    }

    private fun onGetAuthorDataSuccess(data: AuthorResponse?) {
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
        viewModelScope.launch {
            getAuthorPhotosUseCase(
                username = authorUsername,
                page = page
            ).collect {
                handleApiResponse(
                    source = it,
                    onSuccess = { data -> onGetAuthorPhotosSuccess(data) },
                    onError = { error -> onGetAuthorPhotosError(error) }
                )
            }
        }
    }

    private fun onGetAuthorPhotosSuccess(data: List<AuthorPhotosResponse>?) {
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
        page++
        viewModelScope.launch {
            getAuthorPhotosUseCase(
                username = authorUsername,
                page = page
            ).collect {
                handleApiResponse(
                    source = it,
                    onSuccess = { data -> onGetMorePhotosSuccess(data) },
                    onError = { onGetMorePhotosError() }
                )
            }
        }
    }

    private fun onGetMorePhotosSuccess(data: List<AuthorPhotosResponse>?) {
        data?.let { response ->
            _authorPhotos.addAll(response)
            return
        }
        _showErrorToast.postValue(Event(Unit))
    }

    private fun onGetMorePhotosError() {
        _showErrorToast.postValue(Event(Unit))
    }

}