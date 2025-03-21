package com.polaris.clipboard.state

sealed class  ClipboardSaveState {
    data object Initialize:ClipboardSaveState()
    data object ClipboardCrawlingInfo : ClipboardSaveState()
    data object ClipboardSaveLoading : ClipboardSaveState()
    data object ClipboardSaveSuccess : ClipboardSaveState()
    data object ClipboardSaveFailure: ClipboardSaveState()


}