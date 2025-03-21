package com.polaris.shared.state

import com.polaris.model.model.ClipboardFolder

sealed class MainState {
    object Initialize : MainState()

    data class Error(val message: String) : MainState()
    data class Complete(val clipboardFolder: ClipboardFolder) : MainState()
}



