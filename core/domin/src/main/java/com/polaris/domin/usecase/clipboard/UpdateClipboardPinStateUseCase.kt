package com.polaris.domin.usecase.clipboard

import com.polaris.data.local.ClipboardItem
import com.polaris.data.repository.ClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateClipboardPinStateUseCase @Inject constructor(
    private val clipboardRepository: ClipboardRepository
){
    suspend fun execute(
        itemId: Int,
        pinState:Boolean,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {
            onComplete()
            emitAll(clipboardRepository.updatePinStatus(itemId,pinState))


        }
    }
}