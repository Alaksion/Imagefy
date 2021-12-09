package br.com.alaksion.myapplication.ui.home.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.utils.Event
import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.network.client.domain.usecase.GetAuthorizationHeaderUseCase
import br.com.alaksion.myapplication.domain.usecase.GetStoredUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAuthorizationHeaderUseCase: GetAuthorizationHeaderUseCase,
    private val getStoredUserDataUseCase: GetStoredUserDataUseCase
) : BaseViewModel() {

    private var _isUserLogged = MutableLiveData<Event<Boolean>>()
    val isUserLogged: LiveData<Event<Boolean>>
        get() = _isUserLogged

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
            _isUserLogged.postValue(Event(false))
        }
    }

    private fun getCurrentData() {
        viewModelScope.launch {
            getStoredUserDataUseCase().collect {
                _currentUserData.postValue(Event(it))
                _isUserLogged.postValue(Event(true))
            }
        }
    }

}