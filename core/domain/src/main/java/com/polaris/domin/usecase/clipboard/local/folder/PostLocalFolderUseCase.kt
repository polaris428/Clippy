package com.polaris.domin.usecase.clipboard.local.folder

import com.polaris.database.model.toEntity
import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.model.model.ClipboardFolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostLocalFolderUseCase @Inject constructor(
    private val localClipboardRepository: LocalClipboardRepository,

){
    suspend fun execute(
        folder: List<ClipboardFolder>,

        ): Flow<Boolean> {


        return flow {
            emitAll(localClipboardRepository.insertFolder(folder.map { it.toEntity() }))



        }
    }
}