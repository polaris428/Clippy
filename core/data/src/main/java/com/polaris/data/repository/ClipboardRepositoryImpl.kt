package com.polaris.data.repository

import android.util.Log
import com.polaris.data.local.ClipboardDao
import com.polaris.data.local.ClipboardItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class ClipboardRepositoryImpl  @Inject constructor(
    private val clipboardDao:ClipboardDao
) :ClipboardRepository{
    override suspend fun insert(item: ClipboardItem) :Flow<Boolean>{
        Log.e("polaris428","저장")
        clipboardDao.insertClipboardItem(item)
        return flowOf(true)
    }

    override suspend fun getAll(): Flow<List<ClipboardItem>> {
       return clipboardDao.getAllClipboardItems()
    }

    override suspend fun delete(itemId: Int):Flow<Boolean> {

        return   flowOf(clipboardDao.deleteClipboardItem(itemId)>0)
    }

    override suspend fun updatePinStatus(itemId: Int, pinState: Boolean):Flow<Boolean> {
        val test = flowOf(clipboardDao.updatePinStatus(itemId,pinState)>0)
        Log.e("polaris428",test.toString())
        return   test
    }

    override suspend fun updateClipboardItem(clipboardItem: ClipboardItem) :Flow<Boolean> {
        val test = flowOf(clipboardDao.updateClipboardItem(clipboardItem)>0)
        Log.e("polaris428",test.toString())
        return   test
    }

    override suspend fun clearAll() {
       // clipboardDao.clearClipboardHistory()
    }
}
