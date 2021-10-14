package br.com.alaksion.myapplication.ui.authentication.authhandler

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.common.utils.Event
import br.com.alaksion.myapplication.domain.model.AuthResponse
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.model.CurrentUserResponse
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.domain.usecase.GetCurrentUsernameUseCase
import br.com.alaksion.myapplication.domain.usecase.StoreAuthTokenUseCase
import br.com.alaksion.myapplication.domain.usecase.ValidateLoginUseCase
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthHandlerViewModel @Inject constructor(
    private val validateLoginUseCase: ValidateLoginUseCase,
    private val storeAuthTokenUseCase: StoreAuthTokenUseCase,
    private val getCurrentUsernameUseCase: GetCurrentUsernameUseCase,
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase,
    private val userData: CurrentUserData
) : BaseViewModel() {

    private val _authenticationResult: MutableState<ViewState<Unit>> =
        mutableStateOf(ViewState.Loading())
    val authenticationResult: State<ViewState<Unit>>
        get() = _authenticationResult

    private val _handleNavigationSuccess = MutableLiveData<Event<Unit>>()
    val handleNavigationSuccess: LiveData<Event<Unit>>
        get() = _handleNavigationSuccess


    fun authenticateUser(authCode: String?) {
        authCode?.let { code ->
            viewModelScope.launch {
                handleApiResponse(
                    source = validateLoginUseCase(code),
                    onError = { onApiError() },
                    onSuccess = { data -> onAuthenticateUserSuccess(data) }
                )
            }
            return
        }
        _authenticationResult.value = ViewState.Error()
    }

    private fun onAuthenticateUserSuccess(data: AuthResponse?) {
        data?.let { response ->
            storeAuthTokenUseCase(response.accessToken)
            getCurrentUsername()
            return
        }
        _authenticationResult.value = ViewState.Error()
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
        _authenticationResult.value = ViewState.Error()
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
            userData.apply {
                userName = response.username
                name = response.name
                followersCount = response.followers
                followingCount = response.following
                profileImageUrl = response.profileImage
            }
            _handleNavigationSuccess.postValue(Event(Unit))
            return
        }
        _authenticationResult.value = ViewState.Error()
    }

    private fun onApiError() {
        _authenticationResult.value = ViewState.Error()
    }


}