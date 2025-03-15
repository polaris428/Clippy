package com.polaris.domin.usecase.clipboard.clipboard



import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.ClipboardItem

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


import javax.inject.Inject

class GetClipboardAllUseCase @Inject constructor(
    private val localClipboardRepository: LocalClipboardRepository,
    private val remoteClipboardRepository: RemoteClipboardRepository
) {
    suspend fun execute(
        isLogin:Boolean,
        onComplete: () -> Unit,
        ): Flow<List<ClipboardItem>> {


        return flow {
            onComplete()
            if (isLogin){
                emitAll(remoteClipboardRepository.getAll())
            }else{
                emitAll(localClipboardRepository.getAll())

            }


        }
    }
}