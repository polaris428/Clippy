package com.polaris.model.model

import com.polaris.model.dto.ClipboardFolderDTO

data class ClipboardFolder(
    var id: String="",
    var timestamp: Long = System.currentTimeMillis(),
    var name: String="",
    var owner: String="",
)

fun ClipboardFolder.toDTO():ClipboardFolderDTO{
    return ClipboardFolderDTO(id = id,timestamp=timestamp,name =name,owner= owner)
}
