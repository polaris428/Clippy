package com.polaris.domin.usecase.clipboard.remote.clipboard

import com.polaris.domin.repository.LocalClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
//TODO 파이어베이스 코드로 변경
class UpdateClipboardPinStateUseCase @Inject constructor(
    private val clipboardRepository: LocalClipboardRepository
){
    suspend fun execute(
        timestamp: Long,
        pinState:Boolean,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {
            onComplete()
            emitAll(clipboardRepository.updatePinStatus(timestamp,pinState))


        }
    }
}