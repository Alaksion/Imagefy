package br.com.alaksion.myapplication.ui.home.authordetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.alaksion.myapplication.common.network.NetworkError
import br.com.alaksion.myapplication.common.ui.BaseViewModel
import br.com.alaksion.myapplication.common.ui.ViewState
import br.com.alaksion.myapplication.domain.model.AuthorResponse
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorDetailsViewModel @Inject constructor(
    private val getAuthorProfileUseCase: GetAuthorProfileUseCase
) : BaseViewModel() {

    private val _authorData: MutableState<ViewState<AuthorResponse>> =
        mutableStateOf(ViewState.Loading())
    val authorData: State<ViewState<AuthorResponse>>
        get() = _authorData

    fun getAuthorProfileData(authorUsername: String) {
        viewModelScope.launch {
            handleApiResponse(
                source = getAuthorProfileUseCase(authorUsername),
                onError = { error -> onGetAuthorDataError(error) },
                onSuccess = { data -> onGetAuthorDataSuccess(data) }
            )
        }
    }

    private fun onGetAuthorDataSuccess(data: AuthorResponse?) {
        data?.let { response -> _authorData.value = ViewState.Ready(response) }
    }

    private fun onGetAuthorDataError(error: NetworkError) {}

}