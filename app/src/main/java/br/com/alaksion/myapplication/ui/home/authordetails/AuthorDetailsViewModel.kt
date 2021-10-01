package br.com.alaksion.myapplication.ui.home.authordetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

    private val _authorPhotos: MutableState<ViewState<List<AuthorPhotosResponse>>> =
        mutableStateOf(ViewState.Loading())
    val authorPhotos: State<ViewState<List<AuthorPhotosResponse>>>
        get() = _authorPhotos

    private val _isAuthorPhotosLoading = mutableStateOf(true)
    val isAuthorPhotosLoading: State<Boolean>
        get() = _isAuthorPhotosLoading

    private val _isAuthorPhotosError = mutableStateOf(false)
    val isAuthorPhotosError: State<Boolean>
        get() = _isAuthorPhotosError

    private var authorUsername: String = ""
    private var page = 0

    fun getAuthorProfileData(authorUsername: String) {
        this.authorUsername = authorUsername

        viewModelScope.launch {
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

    private fun onGetAuthorDataError(error: NetworkError) {}

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
        data?.let { response -> _authorPhotos.value = ViewState.Ready(response) }
    }

    private fun onGetAuthorPhotosError(error: NetworkError) {}

}