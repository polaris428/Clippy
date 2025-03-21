package com.polaris.main.state

import com.polaris.model.model.ClipboardFolder

sealed class MainState {
    object Initialize : MainState()
    object ClipBoardDateLoading : MainState()
    data class Error(val message: String) : MainState()
    data class Complete(val clipboardFolder: ClipboardFolder) : MainState()
}



