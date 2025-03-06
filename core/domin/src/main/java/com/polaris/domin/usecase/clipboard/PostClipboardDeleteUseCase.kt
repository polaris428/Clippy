package com.polaris.domin.usecase.clipboard

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
        id:Int,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {
            Log.e("polaris0428","흐으으음")
            onComplete()
            emitAll(clipboardRepository.delete(id))
            Log.e("polaris0428","흐으으음1")

        }
    }
}
