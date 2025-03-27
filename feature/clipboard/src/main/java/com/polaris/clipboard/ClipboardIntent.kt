package com.polaris.clipboard

import com.polaris.model.model.ClipboardItem


sealed class ClipboardIntent {
    data class getUrlCrawlingInfo(val url:String):ClipboardIntent()
    object postClipboarInsertIntent: ClipboardIntent()

    object getLocalClipboardFolderName: ClipboardIntent()

}