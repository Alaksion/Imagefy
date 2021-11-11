package br.com.alaksion.myapplication.ui.home.authhandler

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
import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.myapplication.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthHandlerViewModel @Inject constructor(
    private val validateLoginUseCase: ValidateLoginUseCase,
    private val storeAuthTokenUseCase: StoreAuthTokenUseCase,
    private val getCurrentUsernameUseCase: GetCurrentUsernameUseCase,
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase,
    private val storeUserDataUseCase: StoreUserDataUseCase
) : BaseViewModel() {

    private val _authenticationState: MutableState<ViewState<Unit>> =
        mutableStateOf(ViewState.Loading())
    val authenticationState: State<ViewState<Unit>>
        get() = _authenticationState

    private val _handleNavigationSuccess = MutableLiveData<Event<Unit>>()
    val handleNavigationSuccess: LiveData<Event<Unit>>
        get() = _handleNavigationSuccess

    private var _currentUserData = MutableLiveData<Event<StoredUser>>()
    val currentUserData: LiveData<Event<StoredUser>>
        get() = _currentUserData


    fun authenticateUser(authCode: String?) {
        authCode?.let { code ->
            viewModelScope.launch {
                validateLoginUseCase(code).collect {
                    handleApiResponse(
                        source = it,
                        onError = { onApiError() },
                        onSuccess = { data -> onAuthenticateUserSuccess(data) }
                    )
                }
            }
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
        viewModelScope.launch {
            getCurrentUsernameUseCase().collect {
                handleApiResponse(
                    source = it,
                    onError = { onApiError() },
                    onSuccess = { data -> onGetCurrentUsernameSuccess(data) }
                )
            }
        }
    }

    private fun onGetCurrentUsernameSuccess(data: CurrentUserResponse?) {
        data?.let { response ->
            getCurrentUserData(response.username)
            return
        }
        _authenticationState.value = ViewState.Error()
    }

    private fun getCurrentUserData(username: String) {
        viewModelScope.launch {
            getAuthorProfileUseCase(username).collect {
                handleApiResponse(
                    source = it,
                    onError = { onApiError() },
                    onSuccess = { data -> onGetCurrentUserDataSuccess(data) }
                )
            }
        }
    }

    private fun onGetCurrentUserDataSuccess(data: AuthorResponse?) {
        data?.let { response ->
            val userData = StoredUser(
                userName = response.username,
                name = response.name,
                followersCount = response.followers,
                followingCount = response.following,
                profileImageUrl = response.profileImage,
            )
            _currentUserData.postValue(Event(userData))
            _handleNavigationSuccess.postValue(Event(Unit))
            viewModelScope.launch {
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