package com.polaris.model.response

import com.polaris.model.model.ClipboardItem

data class ClipboardItemResponse(
    var timestamp: Long = System.currentTimeMillis(),
    var type: String = "",  // "text" 또는 "url" 구분
    val url: String? = null,
    var title: String = "",
    val itemId: String = "",
    val faviconUrl: String? = null,
    val pinned: Boolean = false
)

fun ClipboardItemResponse.toModel() :ClipboardItem{
    return ClipboardItem(
        timestamp = timestamp,
        type = type,
        url = url,
        title = title,
        itemId = itemId,
        faviconUrl = faviconUrl,
        isPinned = pinned
    )

}

