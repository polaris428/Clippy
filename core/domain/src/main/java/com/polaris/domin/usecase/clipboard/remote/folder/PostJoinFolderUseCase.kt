package com.polaris.domin.usecase.clipboard.remote.folder

import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.toDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostJoinFolderUseCase @Inject constructor(
    private val remoteClipboardRepository: RemoteClipboardRepository
){
    suspend fun execute(
        folderList: List<String>,
        id:String,
        ): Flow<Boolean> {


        return flow {

            emitAll(remoteClipboardRepository.joinFolder(id))


        }
    }
}