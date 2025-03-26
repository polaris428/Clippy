package com.polaris.model.response

import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem

data class ClipboardFolderResponse(
    var id: String = "",
    var timestamp: Long = 0L,
    var name: String = "",
    var owner: String = "",
    var share:Boolean = false,
    var clipboard_dateList: Map<String, ClipboardItemResponse> = emptyMap()

)

fun ClipboardFolderResponse.toModel():ClipboardFolder{
    return ClipboardFolder(clipboardDateList =  clipboard_dateList.map { it.value.toModel() },id=id, name = name, owner = owner)
}