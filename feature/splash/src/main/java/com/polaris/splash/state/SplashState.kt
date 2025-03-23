package com.polaris.splash.state



sealed class SplashState {
    object Initialize : SplashState()
    object Loading : SplashState()
    object ApiSuccess : SplashState()
    object Complete : SplashState()
    data class Error(val message: String) : SplashState()


}