package br.com.alaksion.myapplication.ui.home

import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.extensions.invert
import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.myapplication.domain.usecase.GetCurrentDarkModeConfigUseCase
import br.com.alaksion.myapplication.domain.usecase.StoreDarkModeConfigUseCase
import br.com.alaksion.myapplication.ui.model.BaseViewModel
import br.com.alaksion.myapplication.ui.navigator.HomeScreen
import br.com.alaksion.network.client.domain.usecase.ClearAuthTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val clearAuthTokenUseCase: ClearAuthTokenUseCase,
    private val getCurrentDarkModeConfigUseCase: GetCurrentDarkModeConfigUseCase,
    private val storeCurrentDarkModeConfigUseCase: StoreDarkModeConfigUseCase
) : BaseViewModel() {

    private val _currentUserData = MutableStateFlow(StoredUser())
    val currentUserData: StateFlow<StoredUser>
        get() = _currentUserData

    private val _isConfigDarkMode = MutableStateFlow(false)
    val isConfigDarkMode: StateFlow<Boolean>
        get() = _isConfigDarkMode

    private val _isBottomNavVisible = MutableStateFlow(false)
    val isBottomNavVisible: StateFlow<Boolean>
        get() = _isBottomNavVisible

    private val _isNavDrawerEnabled = MutableStateFlow(false)
    val isNavDrawerEnabled: StateFlow<Boolean>
        get() = _isNavDrawerEnabled

    init {
        getCurrentDarkModeConfig()
    }

    fun clearAuthToken() {
        clearAuthTokenUseCase()
    }

    fun updateUserData(current: StoredUser) {
        _currentUserData.value = current
    }

    private fun getCurrentDarkModeConfig() {
        viewModelScope.launch {
            getCurrentDarkModeConfigUseCase().collect {
                _isConfigDarkMode.value = it
            }
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            _isConfigDarkMode.invert()
            storeCurrentDarkModeConfigUseCase(_isConfigDarkMode.value)
        }
    }

    fun showOrHideBottomNav(currentRoute: String?) {
        _isBottomNavVisible.value =
            currentRoute == HomeScreen.PhotosList().route || currentRoute == HomeScreen.SearchPhotos().route
    }

    fun enableOrDisableNavDrawer(currentRoute: String?) {
        _isNavDrawerEnabled.value =
            currentRoute == HomeScreen.PhotosList().route || currentRoute == HomeScreen.SearchPhotos().route
    }

}
