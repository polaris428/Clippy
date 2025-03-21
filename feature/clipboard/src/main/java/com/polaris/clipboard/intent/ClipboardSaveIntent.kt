package com.polaris.clipboard.intent

import com.polaris.model.model.ClipboardItem

sealed class ClipboardSaveIntent {
    data class getUrlCrawlingInfo(val url:String):ClipboardSaveIntent()
    data class postClipboarInsertIntent (val folderId: String,val clipboardItem: ClipboardItem): ClipboardSaveIntent()
}