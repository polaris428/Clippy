package com.polaris.domin.usecase.clipboard.local.folder

import com.polaris.database.model.toModel
import com.polaris.database.model.toModelList
import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.FolderInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocalClipboardFolderNameUseCase  @Inject constructor(
    private val localClipboardRepository: LocalClipboardRepository,

    ) {
    suspend fun execute(): Flow<List<FolderInfo>> {


        return flow {

            emitAll(localClipboardRepository.getAllFolderNamesList().map { it.toModelList() })


        }
    }
}


