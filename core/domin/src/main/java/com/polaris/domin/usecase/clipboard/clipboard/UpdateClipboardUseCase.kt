package com.polaris.domin.usecase.clipboard.clipboard


import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.model.ClipboardItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateClipboardUseCase  @Inject constructor(
    private val clipboardRepository: LocalClipboardRepository
){
    suspend fun execute(
        clipboardItem: ClipboardItem,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {
            onComplete()
            emitAll(clipboardRepository.updateClipboardItem(clipboardItem))


        }
    }
}
