package com.polaris.domin.usecase.clipboard

import com.polaris.data.local.ClipboardItem
import com.polaris.data.repository.ClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostClipboardDeleteUseCase@Inject constructor(
    private val clipboardRepository: ClipboardRepository
) {
    suspend fun execute(
        id:Int,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {
            onComplete()
            emitAll(clipboardRepository.delete(id))


        }
    }
}
