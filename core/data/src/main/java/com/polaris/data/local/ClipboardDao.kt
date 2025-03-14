package com.polaris.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.polaris.model.ClipboardItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipboardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertClipboardItem(item: ClipboardItem)

    @Query("SELECT * FROM clipboard_history ORDER BY timestamp DESC")
     fun getAllClipboardItems(): Flow<List<ClipboardItem>>

    @Query("DELETE FROM clipboard_history WHERE timestamp = :timestamp")
     fun deleteClipboardItem(timestamp: Long) :Int

    @Query("UPDATE clipboard_history SET isPinned = :isPinned WHERE timestamp = :timestamp")
    suspend fun updatePinStatus(timestamp: Long, isPinned: Boolean) :Int

    @Update
    suspend fun updateClipboardItem(item: ClipboardItem): Int



    @Query("DELETE FROM clipboard_history")
     fun clearClipboardHistory()


}