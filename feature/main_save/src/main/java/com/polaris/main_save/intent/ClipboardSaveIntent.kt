package com.polaris.main_save.intent

import com.polaris.model.model.ClipboardItem

sealed class ClipboardSaveIntent {

    data class postClipboarInsertIntent (val folderId: String,val clipboardItem: ClipboardItem): ClipboardSaveIntent()

}