package com.polaris.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.polaris.database.model.ClipboardFolderEntity


@Dao
interface ClipboardFolderDao {
    @Insert
    suspend fun insertFolder(folder: ClipboardFolderEntity)

    @Query("SELECT * FROM clipboard_folders ORDER BY timestamp DESC")
    fun getAllFolders(): List<ClipboardFolderEntity>

    @Query("DELETE FROM clipboard_folders WHERE id = :folderId")
    suspend fun deleteFolder(folderId: Long)
}
