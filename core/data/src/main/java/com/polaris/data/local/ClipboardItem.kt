package com.polaris.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clipboard_history")
data class ClipboardItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var type: String="",  // "text" 또는 "url" 구분
    val url: String?=null,
    var title: String = "",
    val faviconUrl: String? = null,
    var timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)