package com.polaris.domin.usecase.clipboard.remote.clipboard

import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
//TODO 파이어베이스 코드로 변경
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