package com.polaris.clipboard_edit.intent

import com.polaris.model.model.ClipboardItem

sealed class ClipboardEditIntent {
    data class postClipboarInsertIntent(  val folderId: String,val clipboardItem: ClipboardItem):ClipboardEditIntent()

    object getLocalClipboardFolderName: ClipboardEditIntent()

}