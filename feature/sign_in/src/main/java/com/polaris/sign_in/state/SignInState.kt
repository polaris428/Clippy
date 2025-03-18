package com.polaris.sign_in.state

import com.polaris.model.model.ClipboardFolder

sealed class SignInState {
    object Initialize : SignInState()
    object Loading : SignInState()
    object SignInSuccess : SignInState()
    data class Error(val message: String) : SignInState()
    data class Complete(val clipboardFolder: ClipboardFolder) : SignInState()
}