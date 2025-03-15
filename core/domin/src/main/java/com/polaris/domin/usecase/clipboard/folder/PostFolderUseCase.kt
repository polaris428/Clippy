package com.polaris.domin.usecase.clipboard.folder

import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.ClipboardFolder
import com.polaris.model.ClipboardItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostFolderUseCase @Inject constructor(
    private val localClipboardRepository: LocalClipboardRepository,
    private val remoteClipboardRepository: RemoteClipboardRepository
){
    suspend fun execute(
        isLogin: Boolean,
        folder: ClipboardFolder,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {
            onComplete()
            if (isLogin){
                emitAll(remoteClipboardRepository.insertFolder(folder))
            }else{
                emitAll(localClipboardRepository.insertFolder(folder))

            }


        }
    }
}