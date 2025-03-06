package com.polaris.shared.intent

import com.polaris.model.ClipboardItem


sealed class MainIntent {
    object getAllClipboardListIntent: MainIntent()
    object postClipboarInsertIntent : MainIntent()
    object updateClipboarIntent:MainIntent()
    data class postClipboardMigrationUseCase(val list: List<ClipboardItem>):MainIntent()

}