package com.polaris.domin.usecase.clipboard.remote.folder

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
    suspend fun execute(
        oldFolders: List<ClipboardFolder>,
        newFolders: List<ClipboardFolder>
    ): Flow<Boolean> {


        return flow {
            val oldMap = oldFolders.associateBy { it.id }

            val toInsert = mutableListOf<ClipboardFolder>()
            val toUpdate = mutableListOf<ClipboardFolder>()

            for (new in newFolders) {
                val old = oldMap[new.id]
                when {
                    old == null -> toInsert.add(new) // 새 폴더
                    old.clipboardDateList != new.clipboardDateList -> toUpdate.add(new) // 내용 바뀜
                    // else: 그대로니까 무시
                }
            }

            var result = true

            if (toInsert.isNotEmpty()) {
                localClipboardRepository.insertFolder(toInsert.map { it.toEntity() }).collect {
                    result = result && it
                }
            }

            if (toUpdate.isNotEmpty()) {
                localClipboardRepository.updateFolder(toUpdate.map { it.toEntity() }).collect {
                    result = result && it
                }
            }

            emit(result)
        }


    }
}