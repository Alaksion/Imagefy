package br.com.alaksion.myapplication.ui.home.authhandler

import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.ui.EventViewModel
import br.com.alaksion.myapplication.common.ui.ViewModelEvent
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.model.CurrentUserResponse
import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.domain.usecase.GetCurrentUsernameUseCase
import br.com.alaksion.myapplication.domain.usecase.StoreUserDataUseCase
import br.com.alaksion.myapplication.domain.usecase.ValidateLoginUseCase
import br.com.alaksion.network.client.domain.usecase.StoreAuthTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthHandlerEvents() : ViewModelEvent {
    class NavigateToSuccess() : AuthHandlerEvents()
    class UpdateUserData(val data: StoredUser) : AuthHandlerEvents()
}

@HiltViewModel
class AuthHandlerViewModel @Inject constructor(
    private val validateLoginUseCase: ValidateLoginUseCase,
    private val storeAuthTokenUseCase: StoreAuthTokenUseCase,
    private val getCurrentUsernameUseCase: GetCurrentUsernameUseCase,
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase,
    private val storeUserDataUseCase: StoreUserDataUseCase
) : EventViewModel<AuthHandlerEvents>() {

    private val _authenticationState: MutableStateFlow<ViewState<Unit>> =
        MutableStateFlow(ViewState.Loading())
    val authenticationState: StateFlow<ViewState<Unit>>
        get() = _authenticationState


    fun authenticateUser(authCode: String?) {
        authCode?.let { code ->
            handleApiResponse(
                source = { validateLoginUseCase(code) },
                onError = { onApiError() },
                onSuccess = { data -> onAuthenticateUserSuccess(data) }
            )
            return
        }
        _authenticationState.value = ViewState.Error()
    }


    private fun onAuthenticateUserSuccess(data: AuthResponse?) {
        data?.let { response ->
            storeAuthTokenUseCase(response.accessToken)
            getCurrentUsername()
            return
        }
        _authenticationState.value = ViewState.Error()
    }

    private fun getCurrentUsername() {
        handleApiResponse(
            source = { getCurrentUsernameUseCase() },
            onError = { onApiError() },
            onSuccess = { data -> onGetCurrentUsernameSuccess(data) }
        )
    }


    private fun onGetCurrentUsernameSuccess(data: CurrentUserResponse?) {
        data?.let { response ->
            getCurrentUserData(response.username)
            return
        }
        _authenticationState.value = ViewState.Error()
    }

    private fun getCurrentUserData(username: String) {
        handleApiResponse(
            source = { getAuthorProfileUseCase(username) },
            onError = { onApiError() },
            onSuccess = { data -> onGetCurrentUserDataSuccess(data) }
        )
    }


    private fun onGetCurrentUserDataSuccess(data: AuthorResponse?) {
        data?.let { response ->
            viewModelScope.launch {
                val userData = StoredUser(
                    userName = response.username,
                    name = response.name,
                    followersCount = response.followers,
                    followingCount = response.following,
                    profileImageUrl = response.profileImage,
                )
                sendEvent(AuthHandlerEvents.UpdateUserData(userData))
                sendEvent(AuthHandlerEvents.NavigateToSuccess())
                storeUserDataUseCase(userData)
            }
            return
        }
        _authenticationState.value = ViewState.Error()
    }

    private fun onApiError() {
        _authenticationState.value = ViewState.Error()
    }
}

