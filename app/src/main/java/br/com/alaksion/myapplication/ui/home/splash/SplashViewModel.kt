package br.com.alaksion.myapplication.ui.home.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.ui.EventViewModel
import br.com.alaksion.myapplication.common.ui.ViewModelEvent
import br.com.alaksion.myapplication.common.utils.Event
import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.myapplication.domain.usecase.GetStoredUserDataUseCase
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

    private var _currentUserData = MutableLiveData<Event<StoredUser>>()
    val currentUserData: LiveData<Event<StoredUser>>
        get() = _currentUserData

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