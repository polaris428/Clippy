package com.polaris.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.polaris.model.dto.ClipboardFolderDTO
import com.polaris.model.dto.ClipboardItemDTO
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.response.ClipboardFolderResponse

@Entity(tableName = "clipboard_folders")
data class ClipboardFolderEntity(
    @PrimaryKey var id: String = "",
    var timestamp: Long = System.currentTimeMillis(),
    var name: String = "",
    var owner: String = "",


    )

fun ClipboardFolderEntity.toResponse(): ClipboardFolderResponse {
    return ClipboardFolderResponse(
        id = id,
        timestamp = timestamp,
        name = name,
        owner = owner
    )
}

fun ClipboardFolderDTO.toEntity(): ClipboardFolderResponse {
    return ClipboardFolderResponse(
        id = id,
        timestamp = timestamp,
        name = name,
        owner = owner
    )
}

fun ClipboardFolder.toEntity():ClipboardFolderEntity{
    return ClipboardFolderEntity(id =id,timestamp= timestamp,name= name,owner= owner)
}

