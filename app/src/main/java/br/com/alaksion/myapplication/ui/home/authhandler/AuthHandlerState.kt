package br.com.alaksion.myapplication.ui.home.authhandler

sealed class AuthHandlerState {
    object Loading : AuthHandlerState()
    object Error : AuthHandlerState()
}