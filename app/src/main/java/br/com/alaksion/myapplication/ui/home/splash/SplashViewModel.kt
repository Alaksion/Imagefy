package br.com.alaksion.myapplication.ui.home.splash

import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.myapplication.domain.usecase.GetStoredUserDataUseCase
import br.com.alaksion.myapplication.ui.model.BaseViewModel
import br.com.alaksion.network.client.domain.usecase.GetAuthorizationHeaderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashEvent() {
    class NavigateToLogin() : SplashEvent()
    class NavigateToHome() : SplashEvent()
    class UpdateUserData(val user: StoredUser) : SplashEvent()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAuthorizationHeaderUseCase: GetAuthorizationHeaderUseCase,
    private val getStoredUserDataUseCase: GetStoredUserDataUseCase
) : BaseViewModel() {

    private val _events = MutableSharedFlow<SplashEvent>()
    val events: SharedFlow<SplashEvent>
        get() = _events

    // TODO -> Discover why shared flow won't emit to the view when called on init block
//    init {
//        verifyUserIsLogged()
//    }

    fun verifyUserIsLogged() {
        if (getAuthorizationHeaderUseCase().isNotEmpty()) {
            getCurrentData()
        } else {
            viewModelScope.launch {
                produceEvent(SplashEvent.NavigateToLogin())
            }
        }
    }

    private fun getCurrentData() {
        viewModelScope.launch {
            getStoredUserDataUseCase().collect {
                produceEvent(SplashEvent.UpdateUserData(it))
                produceEvent(SplashEvent.NavigateToHome())
            }
        }
    }

    private suspend fun produceEvent(event: SplashEvent) {
        _events.emit(event)
    }

}