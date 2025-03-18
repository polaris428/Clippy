package com.polaris.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import com.polaris.model.response.ClipboardFolderResponse
import com.google.gson.Gson

@Entity(tableName = "clipboard_folders")
@TypeConverters(ClipboardItemListConverter::class)
data class ClipboardFolderEntity(
    @PrimaryKey var id: String = "",
    var timestamp: Long = System.currentTimeMillis(),
    var name: String = "",
    var owner: String = "",
    var clipboardDateList: List<ClipboardItem> = emptyList()

    )



class ClipboardItemListConverter {
    @TypeConverter
    fun fromClipboardItemList(value: List<ClipboardItem>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toClipboardItemList(value: String): List<ClipboardItem>? {
        val type = object : TypeToken<List<ClipboardItem>>() {}.type
        return Gson().fromJson(value, type)
    }
}