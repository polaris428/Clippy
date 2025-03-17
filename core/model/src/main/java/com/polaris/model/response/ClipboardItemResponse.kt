package com.polaris.model.response

import com.polaris.model.model.ClipboardItem

data class ClipboardItemResponse(
    var timestamp: Long,
    var type: String,  // "text" 또는 "url" 구분
    val url: String?,
    var title: String,
    val folderId: Long,
    val faviconUrl: String?,
    val isPinned: Boolean
)

fun ClipboardItemResponse.toModel() :ClipboardItem{
    return ClipboardItem(
        timestamp = timestamp,
        type = type,
        url = url,
        title = title,
        folderId = folderId,
        faviconUrl = faviconUrl,
        isPinned = isPinned
    )

}

