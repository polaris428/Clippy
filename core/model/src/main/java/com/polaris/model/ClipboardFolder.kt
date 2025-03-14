package com.polaris.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clipboard_folders")
data class ClipboardFolder(
    @PrimaryKey  var id: Long = System.currentTimeMillis(),
    var timestamp: Long = System.currentTimeMillis(),
    val name: String = ""
)