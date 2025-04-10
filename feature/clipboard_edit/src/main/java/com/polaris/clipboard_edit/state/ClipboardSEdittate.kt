package com.polaris.clipboard_edit.state

sealed class ClipboardEditState {
    data object Initialize: ClipboardEditState()
    object Loading:ClipboardEditState()
    data object ClipboardSaveSuccess : ClipboardEditState()
}