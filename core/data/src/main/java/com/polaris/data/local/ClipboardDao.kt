package com.polaris.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipboardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertClipboardItem(item: com.polaris.model.ClipboardItem)

    @Query("SELECT * FROM clipboard_history ORDER BY timestamp DESC")
     fun getAllClipboardItems(): Flow<List<com.polaris.model.ClipboardItem>>

    @Query("DELETE FROM clipboard_history WHERE id = :itemId")
     fun deleteClipboardItem(itemId: Int) :Int

    @Query("UPDATE clipboard_history SET isPinned = :isPinned WHERE id = :id")
    suspend fun updatePinStatus(id: Int, isPinned: Boolean) :Int

    @Update
    suspend fun updateClipboardItem(item: com.polaris.model.ClipboardItem): Int



    @Query("DELETE FROM clipboard_history")
     fun clearClipboardHistory()


}