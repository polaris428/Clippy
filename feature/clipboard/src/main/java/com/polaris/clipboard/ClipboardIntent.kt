package com.polaris.clipboard

import com.polaris.model.model.ClipboardItem
import com.polaris.model.model.FolderInfo


sealed class ClipboardIntent {
    data class getUrlCrawlingInfo(val url:String):ClipboardIntent()
    data class postClipboarInsertIntent(val folderInfo: FolderInfo): ClipboardIntent()

    object getLocalClipboardFolderName: ClipboardIntent()

}