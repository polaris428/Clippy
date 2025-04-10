package com.polaris.domin.usecase.clipboard.remote.clipboard

import android.util.Log
import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostClipboardDeleteUseCase@Inject constructor(
    private val clipboardRepository: RemoteClipboardRepository
) {
    suspend fun execute(
        folderId: String,
        itemId:String,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {

            onComplete()
            emitAll(clipboardRepository.delete(folderId = folderId,itemId=itemId))


        }
    }
}
