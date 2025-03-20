package com.polaris.model.response

import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem

data class ClipboardFolderResponse(
    var id: String = "",
    var timestamp: Long = 0L,
    var name: String = "",
    var owner: String = "",
    var share:Boolean = false,
    var clipboard_dateList: Map<String, ClipboardItem> = emptyMap()

)

fun ClipboardFolderResponse.toModel():ClipboardFolder{
    return ClipboardFolder(clipboardDateList =  clipboard_dateList.values.toList(),id=id, name = name, owner = owner)
}