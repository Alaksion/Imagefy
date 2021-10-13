package br.com.alaksion.myapplication.ui.home

import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currentUserData: CurrentUserData
) : BaseViewModel() {

    fun getUserData() = currentUserData

}