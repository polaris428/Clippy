package com.polaris.model.dto

import com.polaris.model.model.ClipboardItem

data class ClipboardFolderDTO(
    var id: String ,
    var timestamp: Long = System.currentTimeMillis(),
    var name: String ,
    var owner: String ,
    var isShare:Boolean,
    var clipboard_dateList: List<ClipboardItemDTO>?
)