package com.polaris.data.repository

import android.util.Log
import com.polaris.data.local.ClipboardDao
import com.polaris.data.local.ClipboardFolderDao
import com.polaris.model.ClipboardItem
import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.model.ClipboardFolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class LocalClipboardRepositoryImpl @Inject constructor(
    private val clipboardDao: ClipboardDao,
    private val clipboardFolderDao: ClipboardFolderDao
) : LocalClipboardRepository {
    override suspend fun insert(item: com.polaris.model.ClipboardItem): Flow<Boolean> {
        Log.e("polaris428", "저장")
        clipboardDao.insertClipboardItem(item)
        return flowOf(true)
    }

    override suspend fun getAll(): Flow<List<com.polaris.model.ClipboardItem>> {
        return clipboardDao.getAllClipboardItems()
    }

    override suspend fun delete(itemId: Long): Flow<Boolean> {

        return flowOf(clipboardDao.deleteClipboardItem(itemId) > 0)
    }

    override suspend fun updatePinStatus(timestamp: Long, pinState: Boolean): Flow<Boolean> {
        val test = flowOf(clipboardDao.updatePinStatus(timestamp, pinState) > 0)
        Log.e("polaris428", test.toString())
        return test
    }

    override suspend fun updateClipboardItem(clipboardItem: com.polaris.model.ClipboardItem): Flow<Boolean> {
        val test = flowOf(clipboardDao.updateClipboardItem(clipboardItem) > 0)
        Log.e("polaris428", test.toString())
        return test
    }

    override suspend fun clearAll() {
        // clipboardDao.clearClipboardHistory()
    }

    override suspend fun insertFolder(folder: ClipboardFolder): Flow<Boolean> {
        clipboardFolderDao.insertFolder(folder)
        return flowOf(true)
    }

    override suspend fun getFolderList(): Flow<List<ClipboardFolder>> {
        return flowOf(clipboardFolderDao.getAllFolders())
    }

    override suspend fun upDateFolder() {
        //  TODO("Not yet implemented")
    }
}
