package com.polaris.domin.repository


import com.polaris.model.ClipboardFolder
import com.polaris.model.ClipboardItem
import kotlinx.coroutines.flow.Flow

interface LocalClipboardRepository {
    suspend fun insert(item: ClipboardItem) : Flow<Boolean>
    suspend fun getAll(): Flow<List<ClipboardItem>>

    suspend fun delete(timestamp: Long) :Flow<Boolean>
    suspend fun updatePinStatus(timestamp: Long,pinState:Boolean) :Flow<Boolean>
    suspend fun updateClipboardItem(clipboardItem: ClipboardItem):Flow<Boolean>
    suspend fun clearAll()


    suspend fun insertFolder(folder: ClipboardFolder):Flow<Boolean>
    suspend fun getFolderList():Flow<List<ClipboardFolder>>
    suspend fun upDateFolder()
}
