package com.polaris.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClipboardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClipboardItem(item: ClipboardItem)

    @Query("SELECT * FROM clipboard_history ORDER BY timestamp DESC")
    suspend fun getAllClipboardItems(): List<ClipboardItem>

    @Query("DELETE FROM clipboard_history WHERE id = :itemId")
    suspend fun deleteClipboardItem(itemId: Int)

    @Query("DELETE FROM clipboard_history")
    suspend fun clearClipboardHistory()
}