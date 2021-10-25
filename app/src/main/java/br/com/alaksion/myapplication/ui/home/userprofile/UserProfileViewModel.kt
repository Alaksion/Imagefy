package br.com.alaksion.myapplication.ui.home.userprofile

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
class UserProfileViewModel @Inject constructor(
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase,
    private val getAuthorPhotosUseCase: GetAuthorPhotosUseCase
) : BaseViewModel() {

    private val _userData: MutableState<ViewState<AuthorResponse>> =
        mutableStateOf(ViewState.Loading())
    val userData: State<ViewState<AuthorResponse>>
        get() = _userData

    private val _userPhotosState: MutableState<ViewState<Unit>> =
        mutableStateOf(ViewState.Loading())
    val userPhotosState: State<ViewState<Unit>>
        get() = _userPhotosState

    private val _userPhotos = mutableStateListOf<AuthorPhotosResponse>()
    val userPhotos: SnapshotStateList<AuthorPhotosResponse>
        get() = _userPhotos

    private var authorUsername: String = ""
    private var page = 1

    fun getUserProfileData(authorUsername: String) {
        this.authorUsername = authorUsername

        viewModelScope.launch {
            _userData.value = ViewState.Loading()
            handleApiResponse(
                source = getAuthorProfileUseCase(authorUsername),
                onError = { error -> onGetUserDataError(error) },
                onSuccess = { data -> onGetUserDataSuccess(data) }
            )
        }
    }

    private fun onGetUserDataSuccess(data: AuthorResponse?) {
        data?.let { response ->
            _userData.value = ViewState.Ready(response)
            getUserPhotos()
        }
    }

    private fun onGetUserDataError(error: NetworkError) {
        _userData.value = ViewState.Error(error)
    }

    private fun getUserPhotos() {
        viewModelScope.launch {
            handleApiResponse(
                source = getAuthorPhotosUseCase(
                    username = authorUsername,
                    page = page
                ),
                onSuccess = { data -> onGetUserPhotosSuccess(data) },
                onError = { error -> onGetUserPhotosError(error) }
            )
        }
    }

    private fun onGetUserPhotosSuccess(data: List<AuthorPhotosResponse>?) {
        data?.let { response ->
            _userPhotos.addAll(response)
            _userPhotosState.value = ViewState.Ready(Unit)
        }
    }

    private fun onGetUserPhotosError(error: NetworkError) {
        _userPhotosState.value = ViewState.Error(error)
    }

    fun getMoreUserPhotos() {
        page++
        viewModelScope.launch {
            handleApiResponse(
                source = getAuthorPhotosUseCase(
                    username = authorUsername,
                    page = page
                ),
                onSuccess = { data -> onGetUserPhotosSuccess(data) },
                onError = { error -> onGetUserPhotosError(error) }
            )
        }
    }

}