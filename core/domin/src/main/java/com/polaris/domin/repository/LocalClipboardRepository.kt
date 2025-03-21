package com.polaris.domin.repository



import com.polaris.database.model.ClipboardFolderEntity
import com.polaris.database.model.ClipboardItemEntity
import com.polaris.model.dto.ClipboardFolderDTO
import com.polaris.model.dto.ClipboardItemDTO
import com.polaris.model.response.ClipboardFolderResponse
import com.polaris.model.response.ClipboardItemResponse
import kotlinx.coroutines.flow.Flow

interface LocalClipboardRepository {
    suspend fun insert(item: ClipboardItemEntity) : Flow<Boolean>
    suspend fun getAll():Flow<List<ClipboardFolderEntity>>

    suspend fun delete(timestamp: Long) :Flow<Boolean>
    suspend fun updatePinStatus(timestamp: Long,pinState:Boolean) :Flow<Boolean>
    suspend fun updateClipboardItem(clipboardItem: ClipboardItemEntity):Flow<Boolean>
    suspend fun clearAll()


    suspend fun insertFolder(folder: List<ClipboardFolderEntity>):Flow<Boolean>
    suspend fun updateFolder(folder: List<ClipboardFolderEntity>):Flow<Boolean>
    suspend fun upDateFolder()
}
