package com.polaris.domin.usecase.clipboard.local.folder

import com.polaris.database.model.toModel
import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.model.model.ClipboardFolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocalClipboardFolderNameUseCase  @Inject constructor(
    private val localClipboardRepository: LocalClipboardRepository,

    ) {
    suspend fun execute(): Flow<List<String>> {


        return flow {

            emitAll(localClipboardRepository.getAllFolderNamesList())


        }
    }
}


