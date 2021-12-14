package br.com.alaksion.myapplication.ui.home.userprofile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.ui.EventViewModel
import br.com.alaksion.myapplication.common.ui.ViewModelEvent
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorPhotosResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.usecase.GetAuthorPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.network.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserProfileEvents() : ViewModelEvent {
    class ShowMorePhotosError() : UserProfileEvents()
}

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase,
    private val getAuthorPhotosUseCase: GetAuthorPhotosUseCase
) : EventViewModel<UserProfileEvents>() {

    private val _userData: MutableStateFlow<ViewState<AuthorResponse>> =
        MutableStateFlow(ViewState.Loading())
    val userData: StateFlow<ViewState<AuthorResponse>>
        get() = _userData

    private val _userPhotosState: MutableStateFlow<ViewState<Unit>> =
        MutableStateFlow(ViewState.Loading())
    val userPhotosState: StateFlow<ViewState<Unit>>
        get() = _userPhotosState

    private val _userPhotos = mutableStateListOf<AuthorPhotosResponse>()
    val userPhotos: SnapshotStateList<AuthorPhotosResponse>
        get() = _userPhotos

    private var authorUsername: String = ""
    private var page = 1

    fun getUserProfileData(authorUsername: String) {
        this.authorUsername = authorUsername
        _userData.value = ViewState.Loading()

        handleApiResponse(
            source = { getAuthorProfileUseCase(authorUsername) },
            onError = { error -> onGetUserDataError(error) },
            onSuccess = { data -> onGetUserDataSuccess(data) }
        )

    }


    private fun onGetUserDataSuccess(data: AuthorResponse?) {
        data?.let { response ->
            _userData.value = ViewState.Ready(response)
            getUserPhotos()
            return
        }
        _userData.value = ViewState.Error()
    }

    private fun onGetUserDataError(error: NetworkError) {
        _userData.value = ViewState.Error(error)
    }

    private fun getUserPhotos() {
        handleApiResponse(
            source = {
                getAuthorPhotosUseCase(
                    username = authorUsername,
                    page = page
                )
            },
            onSuccess = { data -> onGetUserPhotosSuccess(data) },
            onError = { error -> onGetUserPhotosError(error) }
        )
    }

    private fun onGetUserPhotosSuccess(data: List<AuthorPhotosResponse>?) {
        data?.let { response ->
            _userPhotos.addAll(response)
            _userPhotosState.value = ViewState.Ready(Unit)
            return
        }
        _userPhotosState.value = ViewState.Error()
    }

    private fun onGetUserPhotosError(error: NetworkError) {
        _userPhotosState.value = ViewState.Error(error)
    }

    fun getMoreUserPhotos() {
        page++
        handleApiResponse(
            source = {
                getAuthorPhotosUseCase(
                    username = authorUsername,
                    page = page
                )
            },
            onSuccess = { data -> getMorePhotosSuccess(data) },
            onError = { getMorePhotosError() }
        )
    }

    private fun getMorePhotosSuccess(data: List<AuthorPhotosResponse>?) {
        data?.let {
            _userPhotos.addAll(it)
            return
        }
        showMorePhotosError()
    }

    private fun getMorePhotosError() {
        showMorePhotosError()
    }

    private fun showMorePhotosError() {
        viewModelScope.launch {
            sendEvent(UserProfileEvents.ShowMorePhotosError())
        }
    }

}