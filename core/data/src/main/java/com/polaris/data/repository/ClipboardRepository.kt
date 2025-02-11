package com.polaris.data.repository

import com.polaris.data.local.ClipboardDao
import com.polaris.data.local.ClipboardItem
import kotlinx.coroutines.flow.Flow
import com.skydoves.sandwich.ApiResponse
interface ClipboardRepository {
    suspend fun insert(item: ClipboardItem) : Flow<Boolean>
    suspend fun getAll(): Flow<List<ClipboardItem>>

    suspend fun delete(itemId: Int)

    suspend fun clearAll()
}
