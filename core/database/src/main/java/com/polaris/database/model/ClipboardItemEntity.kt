package com.polaris.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.polaris.model.dto.ClipboardItemDTO
import com.polaris.model.model.ClipboardItem
import com.polaris.model.response.ClipboardFolderResponse
import com.polaris.model.response.ClipboardItemResponse

@Entity(tableName = "clipboard_history")
data class ClipboardItemEntity(
    @PrimaryKey var timestamp: Long = System.currentTimeMillis(),
    var type: String = "",  // "text" 또는 "url" 구분
    val url: String? = null,
    var title: String = "",
    val itemId: String = "",
    val faviconUrl: String? = null,
    val isPinned: Boolean = false
)

fun ClipboardItemEntity.toResponse(): ClipboardItemResponse {
    return ClipboardItemResponse(
        timestamp = timestamp,
        type = type,
        url = url,
        title = title,
        itemId = itemId,
        faviconUrl = faviconUrl,
        pinned = isPinned

    )
}

fun ClipboardItemDTO.toEntity(): ClipboardItemEntity {
    return ClipboardItemEntity(
        timestamp = timestamp,
        type = type,
        url = url,
        title = title,
        itemId = itemId,
        faviconUrl = faviconUrl,
        isPinned = isPinned
    )

}

fun ClipboardItem.toEntity(): ClipboardItemEntity {
    return ClipboardItemEntity(
        timestamp = timestamp,
        type = type,
        url = url,
        title = title,
        itemId = itemId,
        faviconUrl = faviconUrl,
        isPinned = isPinned
    )

}
