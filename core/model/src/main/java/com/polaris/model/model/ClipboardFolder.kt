package com.polaris.model.model

import com.polaris.model.dto.ClipboardFolderDTO
import com.polaris.model.dto.toDTO

data class ClipboardFolder(
    val id:String="",
    val timestamp: Long = System.currentTimeMillis(),
    val name: String="",
    val owner: String="",
    var clipboardDateList: List<ClipboardItem> = arrayListOf()
)

fun ClipboardFolder.toDTO():ClipboardFolderDTO{
    return ClipboardFolderDTO(id = id,timestamp=timestamp,name =name,owner= owner,clipboard_dateList=clipboardDateList.map { it.toDTO() })
}
