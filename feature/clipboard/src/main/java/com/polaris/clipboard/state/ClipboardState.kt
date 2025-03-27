package com.polaris.clipboard.state

sealed class ClipboardState {
    data object Initialize: ClipboardState()
    data object SiteCrawlingStart: ClipboardState()
    data object SiteCrawlingComplete: ClipboardState()
    data object ClipboardSaveSuccess : ClipboardState()
}