package com.polaris.domin.usecase.clipboard.remote.clipboard

import android.util.Log
import com.polaris.domin.repository.LocalClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostClipboardDeleteUseCase@Inject constructor(
    private val clipboardRepository: LocalClipboardRepository
) {
    suspend fun execute(
        timestamp:Long,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {

            onComplete()
            emitAll(clipboardRepository.delete(timestamp))


        }
    }
}
