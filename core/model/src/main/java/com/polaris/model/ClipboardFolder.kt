package com.polaris.model

import androidx.databinding.adapters.Converters
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "clipboard_folders")
data class ClipboardFolder(
    @PrimaryKey  var id: String ="",
    var timestamp: Long = System.currentTimeMillis(),
    val name: String = "",
    val owner:String="",


)
