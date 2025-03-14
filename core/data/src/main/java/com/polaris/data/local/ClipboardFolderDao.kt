package com.polaris.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.polaris.model.ClipboardFolder
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipboardFolderDao {
    @Insert
    suspend fun insertFolder(folder: ClipboardFolder)

    @Query("SELECT * FROM clipboard_folders ORDER BY timestamp DESC")
    fun getAllFolders(): List<ClipboardFolder>

    @Query("DELETE FROM clipboard_folders WHERE id = :folderId")
    suspend fun deleteFolder(folderId: Long)
}
