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
import br.com.alaksion.myapplication.domain.usecase.StoreAuthTokenUseCase
import br.com.alaksion.myapplication.domain.usecase.ValidateLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthHandlerViewModel @Inject constructor(
    private val validateLoginUseCase: ValidateLoginUseCase,
    private val storeAuthTokenUseCase: StoreAuthTokenUseCase
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
                    onError = { onAuthenticateUserError() },
                    onSuccess = { data -> onAuthenticateUserSuccess(data) }
                )
            }
            return
        }
        _authenticationResult.value = ViewState.Error()
    }

    private fun onAuthenticateUserSuccess(data: AuthResponse?) {
        data?.let { response ->
            _handleNavigationSuccess.postValue(Event(Unit))
            storeAuthTokenUseCase(response.accessToken)
            return
        }
        _authenticationResult.value = ViewState.Error()
    }

    private fun onAuthenticateUserError() {
        _authenticationResult.value = ViewState.Error()
    }

}