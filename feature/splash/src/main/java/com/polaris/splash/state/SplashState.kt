package com.polaris.splash.state

import com.polaris.model.model.ClipboardFolder
import com.polaris.shared.state.MainState

sealed class SplashState {
    object Initialize : SplashState()
    object Loading : SplashState()
    object ApiSuccess : SplashState()
    object Complete : SplashState()
    data class Error(val message: String) : SplashState()


}