package br.com.alaksion.myapplication.ui.home.login

import android.net.Uri
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

    fun getLoginUrl(): Uri = Uri.parse(getAuthUrlUseCase())

}