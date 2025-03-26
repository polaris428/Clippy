package com.polaris.model.model

data class ClipboardItem(
    var timestamp: Long = System.currentTimeMillis(),
    var type: String = "",  // "text" 또는 "url" 구분
    val url: String? = null,
    var title: String = "",
    val itemId: String = "",
    val faviconUrl: String? = null,
    val isPinned: Boolean = false
)
