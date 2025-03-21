package com.polaris.domin.usecase.clipboard.remote.folder

import com.polaris.database.model.toEntity
import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.dto.ClipboardFolderDTO
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.toDTO

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostFolderUseCase @Inject constructor(
    private val localClipboardRepository: LocalClipboardRepository,
    private val remoteClipboardRepository: RemoteClipboardRepository
){
    suspend fun execute(
        folder: ClipboardFolder,

        ): Flow<Boolean> {


        return flow {

            emitAll(remoteClipboardRepository.insertFolder(folder.toDTO()))


        }
    }
}