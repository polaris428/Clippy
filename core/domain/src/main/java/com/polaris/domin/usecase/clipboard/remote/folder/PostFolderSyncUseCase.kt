package com.polaris.domin.usecase.clipboard.remote.folder

import android.util.Log
import com.polaris.database.model.ClipboardFolderEntity
import com.polaris.database.model.toEntity
import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.toDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostFolderSyncUseCase @Inject constructor(
    private val localClipboardRepository: LocalClipboardRepository,

    ) {

    companion object {
        private const val TAG = "PostFolderSync"
    }



    suspend fun execute(
        oldFolders: List<ClipboardFolder>,
        newFolders: List<ClipboardFolder>
    ): Flow<Boolean> = flow {

        Log.d(TAG, "Starting folder sync")
        Log.d(TAG, "Old folders count: ${oldFolders.size}")
        Log.d(TAG, "New folders count: ${newFolders.size}")

        val oldMap = oldFolders.associateBy { it.id }

        val toInsert = mutableListOf<ClipboardFolder>()
        val toUpdate = mutableListOf<ClipboardFolder>()

        for (new in newFolders) {
            val old = oldMap[new.id]
            when {
                old == null -> {
                    Log.d(TAG, "New folder detected: ${new.id}")
                    toInsert.add(new)
                }
                old.clipboardDateList != new.clipboardDateList -> {
                    Log.d(TAG, "Folder content changed: ${new.id}")
                    toUpdate.add(new)
                }
                else -> {
                    Log.d(TAG, "No changes for folder: ${new.id}")
                }
            }
        }

        Log.d(TAG, "Total folders to insert: ${toInsert.size}")
        Log.d(TAG, "Total folders to update: ${toUpdate.size}")

        var result = true

        if (toInsert.isNotEmpty()) {
            Log.d(TAG, "Inserting folders...")
            localClipboardRepository.insertFolder(toInsert.map { it.toEntity() }).collect {
                Log.d(TAG, "Insert result: $it")
                result = result && it
            }
        }

        if (toUpdate.isNotEmpty()) {
            Log.d(TAG, "Updating folders...")
            localClipboardRepository.updateFolder(toUpdate.map { it.toEntity() }).collect {
                Log.d(TAG, "Update result: $it")
                result = result && it
            }
        }

        Log.d(TAG, "Folder sync completed with result: $result")
        emit(result)
    }
}