package com.polaris.domin.repository



import com.polaris.model.dto.ClipboardFolderDTO
import com.polaris.model.dto.ClipboardItemDTO
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.response.ClipboardFolderResponse
import com.polaris.model.response.ClipboardItemResponse
import kotlinx.coroutines.flow.Flow

interface RemoteClipboardRepository {
    suspend fun postDataMigration(itemList: List<ClipboardItemDTO>):Flow<Boolean>
    suspend fun insert(item: ClipboardItemDTO) : Flow<Boolean>
    suspend fun getAll(): Flow<List<ClipboardItemResponse>>

    suspend fun delete(itemId: Int) : Flow<Boolean>
    suspend fun updatePinStatus(itemId: Int,pinState:Boolean) : Flow<Boolean>
    suspend fun updateClipboardItem(clipboardItem: ClipboardItemDTO): Flow<Boolean>
    suspend fun clearAll():Flow<Boolean>

    suspend fun insertFolder(folder: ClipboardFolderDTO):Flow<Boolean>
    suspend fun getFolderList():Flow<List<ClipboardFolderResponse>>
    suspend fun upDateFolder()
}