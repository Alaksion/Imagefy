package br.com.alaksion.myapplication.ui.home.splash

import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.myapplication.domain.usecase.GetStoredUserDataUseCase
import br.com.alaksion.myapplication.ui.model.EventViewModel
import br.com.alaksion.myapplication.ui.model.ViewModelEvent
import br.com.alaksion.network.client.domain.usecase.GetAuthorizationHeaderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashEvent() : ViewModelEvent {
    class NavigateToLogin() : SplashEvent()
    class NavigateToHome() : SplashEvent()
    class UpdateUserData(val user: StoredUser) : SplashEvent()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAuthorizationHeaderUseCase: GetAuthorizationHeaderUseCase,
    private val getStoredUserDataUseCase: GetStoredUserDataUseCase
) : EventViewModel<SplashEvent>() {

    init {
        verifyUserIsLogged()
    }

    private fun verifyUserIsLogged() {
        if (getAuthorizationHeaderUseCase().isNotEmpty()) {
            getCurrentData()
        } else {
            viewModelScope.launch {
                sendEvent(SplashEvent.NavigateToLogin())
            }
        }
    }

    private fun getCurrentData() {
        viewModelScope.launch {
            getStoredUserDataUseCase().collect {
                sendEvent(SplashEvent.UpdateUserData(it))
                sendEvent(SplashEvent.NavigateToHome())
            }
        }
    }

}