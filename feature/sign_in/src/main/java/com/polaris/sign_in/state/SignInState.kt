package com.polaris.sign_in.state

sealed class SignInState {
    object Initialize : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
    data class Error(val message: String) : SignInState()
}