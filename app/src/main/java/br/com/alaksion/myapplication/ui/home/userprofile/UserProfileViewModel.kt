package br.com.alaksion.myapplication.ui.home.userprofile

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.domain.model.Author
import br.com.alaksion.myapplication.domain.model.AuthorPhotos
import br.com.alaksion.myapplication.domain.usecase.GetAuthorPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.ui.model.BaseViewModel
import br.com.alaksion.myapplication.ui.model.ViewState
import br.com.alaksion.network.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserProfileEvents() {
    class ShowMorePhotosError() : UserProfileEvents()
}

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase,
    private val getAuthorPhotosUseCase: GetAuthorPhotosUseCase
) : BaseViewModel() {

    private val _events = MutableSharedFlow<UserProfileEvents>()
    val events: SharedFlow<UserProfileEvents>
        get() = _events

    private val _userData: MutableStateFlow<ViewState<Author>> =
        MutableStateFlow(ViewState.Loading())
    val userData: StateFlow<ViewState<Author>>
        get() = _userData

    private val _userPhotosState: MutableStateFlow<ViewState<Unit>> =
        MutableStateFlow(ViewState.Loading())
    val userPhotosState: StateFlow<ViewState<Unit>>
        get() = _userPhotosState

    private val _userPhotos = mutableStateListOf<AuthorPhotos>()
    val userPhotos: SnapshotStateList<AuthorPhotos>
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


    private fun onGetUserDataSuccess(data: Author?) {
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

    private fun onGetUserPhotosSuccess(data: List<AuthorPhotos>?) {
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

    private fun getMorePhotosSuccess(data: List<AuthorPhotos>?) {
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
            produceEvent(UserProfileEvents.ShowMorePhotosError())
        }
    }

    private suspend fun produceEvent(event: UserProfileEvents) {
        _events.emit(event)
    }


}