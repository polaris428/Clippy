package com.polaris.domin.usecase.clipboard



import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.model.ClipboardItem

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


import javax.inject.Inject

class GetClipboardAllUseCase @Inject constructor(
    private val clipboardRepository: LocalClipboardRepository
) {
    suspend fun execute(
        onComplete: () -> Unit,

        ): Flow<List<ClipboardItem>> {


        return flow {
            onComplete()
            emitAll(clipboardRepository.getAll())


        }
    }
}