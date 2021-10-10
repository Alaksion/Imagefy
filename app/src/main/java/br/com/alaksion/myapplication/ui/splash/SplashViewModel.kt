package br.com.alaksion.myapplication.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.utils.Event
import br.com.alaksion.myapplication.domain.usecase.GetAuthorizationHeaderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAuthorizationHeaderUseCase: GetAuthorizationHeaderUseCase
) : BaseViewModel() {

    private var _isUserLogged = MutableLiveData<Event<Boolean>>()
    val isUserLogged: LiveData<Event<Boolean>>
        get() = _isUserLogged

    fun verifyUserIsLogged() {
        viewModelScope.launch {
            delay(1500)
            _isUserLogged.postValue(Event(getAuthorizationHeaderUseCase().isNotEmpty()))
        }
    }

}