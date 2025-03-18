package com.polaris.shared.intent

import com.polaris.model.model.ClipboardItem


sealed class MainIntent {
    data class getAllClipboardListIntent(val folderId:String): MainIntent()
    data class postClipboarInsertIntent(val folder:String) : MainIntent()
    object updateClipboarIntent:MainIntent()
    data class postClipboardMigrationUseCase(val list: List<ClipboardItem>):MainIntent()

}