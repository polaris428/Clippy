package com.polaris.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clipboard_history")
data class ClipboardItem(
    @PrimaryKey  var timestamp: Long = System.currentTimeMillis(),
    var type: String="",  // "text" 또는 "url" 구분
    val url: String?=null,
    var title: String = "",
    val folderId:Long=0,
    val faviconUrl: String? = null,
    val isPinned: Boolean = false
)