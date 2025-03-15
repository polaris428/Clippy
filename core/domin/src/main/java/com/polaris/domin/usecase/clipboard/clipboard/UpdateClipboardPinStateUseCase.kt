package com.polaris.domin.usecase.clipboard.clipboard

import com.polaris.domin.repository.LocalClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

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