package com.polaris.shared.intent


sealed class MainIntent {
    object getAllClipboardListIntent: MainIntent()
    object postClipboarInsertIntent : MainIntent()
    object updateClipboarIntent:MainIntent()

}