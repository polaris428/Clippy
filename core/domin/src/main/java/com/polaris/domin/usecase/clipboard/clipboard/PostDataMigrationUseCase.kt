package com.polaris.domin.usecase.clipboard.clipboard

import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.ClipboardItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostDataMigrationUseCase @Inject constructor(
    private val clipboardRepository: RemoteClipboardRepository
){
    suspend fun execute(
        item: List<ClipboardItem>,
        onComplete: () -> Unit,

        ): Flow<Boolean> {


        return flow {
            onComplete()
            emitAll(clipboardRepository.postDataMigration(item))


        }
    }
}