package com.polaris.clipboard.state

sealed class ClipboardUiState {
    data object Initialize:ClipboardUiState()
    data object ClipboardSave : ClipboardUiState()
}