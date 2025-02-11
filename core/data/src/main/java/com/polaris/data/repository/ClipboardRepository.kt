package com.polaris.data.repository

import com.polaris.data.local.ClipboardDao
import com.polaris.data.local.ClipboardItem

class ClipboardRepository(private val clipboardDao: ClipboardDao) {
    suspend fun insert(item: ClipboardItem) {
        clipboardDao.insertClipboardItem(item)
    }

    suspend fun getAll(): List<ClipboardItem> {
        return clipboardDao.getAllClipboardItems()
    }

    suspend fun delete(itemId: Int) {
        clipboardDao.deleteClipboardItem(itemId)
    }

    suspend fun clearAll() {
        clipboardDao.clearClipboardHistory()
    }
}
