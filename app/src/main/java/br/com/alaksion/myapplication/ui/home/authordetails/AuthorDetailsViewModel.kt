package br.com.alaksion.myapplication.ui.home.authordetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.usecase.GetAuthorPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private var authorUsername: String = ""
    private var page = 1

    fun getAuthorProfileData(authorUsername: String) {
        this.authorUsername = authorUsername

        viewModelScope.launch {
            _authorData.value = ViewState.Loading()
            handleApiResponse(
                source = getAuthorProfileUseCase(authorUsername),
                onError = { error -> onGetAuthorDataError(error) },
                onSuccess = { data -> onGetAuthorDataSuccess(data) }
            )
        }
    }

    private fun onGetAuthorDataSuccess(data: AuthorResponse?) {
        data?.let { response ->
            _authorData.value = ViewState.Ready(response)
            getAuthorPhotos()
        }
    }

    private fun onGetAuthorDataError(error: NetworkError) {
        _authorData.value = ViewState.Error(error)
    }

    private fun getAuthorPhotos() {
        viewModelScope.launch {
            handleApiResponse(
                source = getAuthorPhotosUseCase(
                    username = authorUsername,
                    page = page
                ),
                onSuccess = { data -> onGetAuthorPhotosSuccess(data) },
                onError = { error -> onGetAuthorPhotosError(error) }
            )
        }
    }

    private fun onGetAuthorPhotosSuccess(data: List<AuthorPhotosResponse>?) {
        data?.let { response ->
            _authorPhotos.addAll(response)
            _authorPhotosState.value = ViewState.Ready(Unit)
        }
    }

    private fun onGetAuthorPhotosError(error: NetworkError) {
        _authorPhotosState.value = ViewState.Error(error)
    }

    fun getMoreAuthorPhotos() {
        page++
        viewModelScope.launch {
            handleApiResponse(
                source = getAuthorPhotosUseCase(
                    username = authorUsername,
                    page = page
                ),
                onSuccess = { data -> onGetAuthorPhotosSuccess(data) },
                onError = { error -> onGetAuthorPhotosError(error) }
            )
        }
    }

}