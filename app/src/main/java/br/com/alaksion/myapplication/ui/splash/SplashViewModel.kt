package br.com.alaksion.myapplication.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.ui.BaseViewModel
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
    private val userData: CurrentUserData
) : BaseViewModel() {

    private var _isUserLogged = MutableLiveData<Event<Boolean>>()
    val isUserLogged: LiveData<Event<Boolean>>
        get() = _isUserLogged

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
            userData.apply {
                userName = response.username
                name = response.name
                followersCount = response.followers
                followingCount = response.following
                profileImageUrl = response.profileImage
            }
            _isUserLogged.postValue(Event(true))
            return
        }
        _isUserLogged.postValue(Event(false))
    }

    private fun onApiError() {
        _isUserLogged.postValue(Event(false))
    }

}