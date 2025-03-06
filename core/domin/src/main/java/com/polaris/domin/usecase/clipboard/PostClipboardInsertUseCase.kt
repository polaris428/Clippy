package com.polaris.domin.usecase.clipboard

import com.polaris.data.local.ClipboardItem
import com.polaris.domin.repository.LocalClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostClipboardInsertUseCase @Inject constructor(
   private val clipboardRepository: LocalClipboardRepository
){
    suspend fun execute(
        item: ClipboardItem,
        onComplete: () -> Unit,

        ):Flow<Boolean> {


        return flow {
            onComplete()
            emitAll(clipboardRepository.insert(item))


        }
    }
}