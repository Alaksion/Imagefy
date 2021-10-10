package br.com.alaksion.myapplication.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.utils.Event
import br.com.alaksion.myapplication.domain.usecase.GetAuthUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getAuthUrlUseCase: GetAuthUrlUseCase
) : BaseViewModel() {

    fun getLoginUrl() = getAuthUrlUseCase()

    private val _isAuthenticationSuccess = MutableLiveData<Event<Unit>>()
    val isAuthenticationSuccess: LiveData<Event<Unit>>
        get() = _isAuthenticationSuccess

    fun setAuthResult() {
        _isAuthenticationSuccess.postValue(Event(Unit))
    }

}