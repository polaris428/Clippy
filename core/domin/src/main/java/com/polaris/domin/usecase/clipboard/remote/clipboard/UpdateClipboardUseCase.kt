package com.polaris.domin.usecase.clipboard.remote.clipboard


import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.database.model.toEntity
import com.polaris.model.dto.ClipboardItemDTO
import com.polaris.model.model.ClipboardItem
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
            emitAll(clipboardRepository.updateClipboardItem(clipboardItem.toEntity()))


        }
    }
}
