package br.com.alaksion.myapplication.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.extensions.invert
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.domain.usecase.ClearAuthTokenUseCase
import br.com.alaksion.myapplication.domain.usecase.GetCurrentDarkModeConfigUseCase
import br.com.alaksion.myapplication.domain.usecase.StoreDarkModeConfigUseCase
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clearAuthTokenUseCase: ClearAuthTokenUseCase,
    private val getCurrentDarkModeConfigUseCase: GetCurrentDarkModeConfigUseCase,
    private val storeCurrentDarkModeConfigUseCase: StoreDarkModeConfigUseCase
) : BaseViewModel() {

    private val _currentUserData = mutableStateOf(CurrentUserData())
    val currentUserData: State<CurrentUserData>
        get() = _currentUserData

    private val _isConfigDarkMode = mutableStateOf(false)
    val isConfigDarkMode: State<Boolean>
        get() = _isConfigDarkMode

    fun toggleDarkMode() {
        viewModelScope.launch {
            _isConfigDarkMode.invert()
            storeCurrentDarkModeConfigUseCase(_isConfigDarkMode.value)
        }
    }

    fun clearAuthToken() {
        clearAuthTokenUseCase()
    }

    fun updateUserData(current: CurrentUserData) {
        _currentUserData.value = current
    }

    fun getCurrentDarkModeConfig(): Flow<Boolean> {
        return getCurrentDarkModeConfigUseCase()
    }
}
