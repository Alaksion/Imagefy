package br.com.alaksion.myapplication.ui.home.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.utils.Event
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.model.CurrentUserResponse
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorizationHeaderUseCase
import br.com.alaksion.myapplication.domain.usecase.GetCurrentUsernameUseCase
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAuthorizationHeaderUseCase: GetAuthorizationHeaderUseCase,
    private val getCurrentUsernameUseCase: GetCurrentUsernameUseCase,
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase,
) : BaseViewModel() {

    private val _authenticationState: MutableState<ViewState<Unit>> =
        mutableStateOf(ViewState.Loading())
    val authenticationState: State<ViewState<Unit>>
        get() = _authenticationState

    private var _isUserLogged = MutableLiveData<Event<Boolean>>()
    val isUserLogged: LiveData<Event<Boolean>>
        get() = _isUserLogged

    private var _currentUserData = MutableLiveData<Event<CurrentUserData>>()
    val currentUserData: LiveData<Event<CurrentUserData>>
        get() = _currentUserData

    fun verifyUserIsLogged() {
        if (getAuthorizationHeaderUseCase().isNotEmpty()) {
            getCurrentUsername()
        } else {
            _isUserLogged.postValue(Event(false))
        }
    }

    private fun getCurrentUsername() {
        viewModelScope.launch {
            handleApiResponse(
                source = getCurrentUsernameUseCase(),
                onError = { onApiError() },
                onSuccess = { data -> onGetCurrentUsernameSuccess(data) }
            )
        }
    }

    private fun onGetCurrentUsernameSuccess(data: CurrentUserResponse?) {
        data?.let { response ->
            getCurrentUserData(response.username)
            return
        }
        _isUserLogged.postValue(Event(false))
    }

    private fun getCurrentUserData(username: String) {
        viewModelScope.launch {
            handleApiResponse(
                source = getAuthorProfileUseCase(username),
                onError = { onApiError() },
                onSuccess = { data -> onGetCurrentUserDataSuccess(data) }
            )
        }
    }

    private fun onGetCurrentUserDataSuccess(data: AuthorResponse?) {
        data?.let { response ->
            _currentUserData.postValue(
                Event(
                    CurrentUserData(
                        userName = response.username,
                        name = response.name,
                        followersCount = response.followers,
                        followingCount = response.following,
                        profileImageUrl = response.profileImage,
                    )
                )
            )
            _isUserLogged.postValue(Event(true))
            return
        }
        _isUserLogged.postValue(Event(false))
    }

    private fun onApiError() {
        _authenticationState.value = ViewState.Error()
    }

}