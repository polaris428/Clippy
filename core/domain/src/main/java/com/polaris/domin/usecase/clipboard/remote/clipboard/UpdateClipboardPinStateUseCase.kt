package com.polaris.domin.usecase.clipboard.remote.clipboard

import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateClipboardPinStateUseCase @Inject constructor(
    private val clipboardRepository: RemoteClipboardRepository
){
    suspend fun execute(
        folderId: String,
        itemId:String,
        pinState:Boolean,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {
            onComplete()
            emitAll(clipboardRepository.updatePinStatus(folderId,itemId,pinState))


        }
    }
}