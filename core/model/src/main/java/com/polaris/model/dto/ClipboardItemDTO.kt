package com.polaris.model.dto

import com.polaris.model.model.ClipboardItem
import com.polaris.model.response.ClipboardFolderResponse


data class ClipboardItemDTO(
    var timestamp: Long = System.currentTimeMillis(),
    var type: String = "",  // "text" 또는 "url" 구분
    val url: String? = null,
    var title: String = "",
    var itemId: String = "",
    val faviconUrl: String? = null,
    val isPinned: Boolean = false
)


fun ClipboardItem.toDTO():ClipboardItemDTO {
    return ClipboardItemDTO(
        timestamp = timestamp,
        type = type,
        url = url,
        title = title,
        itemId = itemId,
        faviconUrl = faviconUrl,
        isPinned = isPinned
    )
}
