package com.polaris.domin.repository


import com.polaris.model.ClipboardFolder
import com.polaris.model.ClipboardItem
import kotlinx.coroutines.flow.Flow

interface RemoteClipboardRepository {
    suspend fun postDataMigration(itemList: List<ClipboardItem>):Flow<Boolean>
    suspend fun insert(item: ClipboardItem) : Flow<Boolean>
    suspend fun getAll(): Flow<List<ClipboardItem>>

    suspend fun delete(itemId: Int) : Flow<Boolean>
    suspend fun updatePinStatus(itemId: Int,pinState:Boolean) : Flow<Boolean>
    suspend fun updateClipboardItem(clipboardItem: ClipboardItem): Flow<Boolean>
    suspend fun clearAll():Flow<Boolean>

    suspend fun insertFolder(folder: ClipboardFolder):Flow<Boolean>
    suspend fun getFolderList():Flow<List<ClipboardFolder>>
    suspend fun upDateFolder()
}