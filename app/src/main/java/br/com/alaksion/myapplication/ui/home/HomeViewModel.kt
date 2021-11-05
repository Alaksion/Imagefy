package br.com.alaksion.myapplication.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.domain.usecase.ClearAuthTokenUseCase
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clearAuthTokenUseCase: ClearAuthTokenUseCase
) : BaseViewModel() {

    private val _currentUserData = mutableStateOf(CurrentUserData())
    val currentUserData: State<CurrentUserData>
        get() = _currentUserData

    fun clearAuthToken() {
        clearAuthTokenUseCase()
    }

    fun updateUserData(current: CurrentUserData) {
        _currentUserData.value = current
    }

}