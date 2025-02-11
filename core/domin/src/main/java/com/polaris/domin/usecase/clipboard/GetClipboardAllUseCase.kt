package com.polaris.domin.usecase.clipboard


import com.polaris.data.local.ClipboardItem
import com.polaris.data.repository.ClipboardRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


import javax.inject.Inject

class GetClipboardAllUseCase @Inject constructor(
    private val clipboardRepository: ClipboardRepository
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