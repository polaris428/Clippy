package com.polaris.data.repository

import android.util.Log
import com.polaris.database.dao.ClipboardDao
import com.polaris.database.dao.ClipboardFolderDao
import com.polaris.database.model.ClipboardFolderEntity
import com.polaris.database.model.ClipboardItemEntity
import com.polaris.database.model.toResponse
import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.model.response.ClipboardFolderResponse
import com.polaris.model.response.ClipboardItemResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalClipboardRepositoryImpl @Inject constructor(
    private val clipboardDao: ClipboardDao,
    private val clipboardFolderDao: ClipboardFolderDao
) : LocalClipboardRepository {
    override suspend fun insert(item: ClipboardItemEntity): Flow<Boolean> {
        Log.e("polaris428", "저장")
        clipboardDao.insertClipboardItem(item)
        return flowOf(true)
    }

    override suspend fun getAll(): Flow<List<ClipboardFolderEntity>> {
       return flowOf( clipboardFolderDao.getAllFolders())

    }


    override suspend fun delete(itemId: Long): Flow<Boolean> {

        return flowOf(clipboardDao.deleteClipboardItem(itemId) > 0)
    }

    override suspend fun updatePinStatus(timestamp: Long, pinState: Boolean): Flow<Boolean> {
        val test = flowOf(clipboardDao.updatePinStatus(timestamp, pinState) > 0)
        Log.e("polaris428", test.toString())
        return test
    }

    override suspend fun updateClipboardItem(clipboardItem: ClipboardItemEntity): Flow<Boolean> {
        val test = flowOf(clipboardDao.updateClipboardItem(clipboardItem) > 0)
        Log.e("polaris428", test.toString())
        return test
    }

    override suspend fun clearAll() {
        // clipboardDao.clearClipboardHistory()
    }

    override suspend fun insertFolder(folder: List<ClipboardFolderEntity>): Flow<Boolean> {
        clipboardFolderDao.insertFolders(folder)
        return flowOf(true)
    }

    override suspend fun updateFolder(folder: List<ClipboardFolderEntity>): Flow<Boolean> {
        clipboardFolderDao.updateFolders(folder)
        return flowOf(true)
    }



    override suspend fun upDateFolder() {
        //  TODO("Not yet implemented")
    }
}
