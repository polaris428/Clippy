package com.polaris.domin.usecase.clipboard.clipboard


import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.model.ClipboardItem
import com.polaris.model.response.ClipboardItemResponse
import com.polaris.model.response.toModel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


import javax.inject.Inject

class GetClipboardAllUseCase @Inject constructor(
    private val localClipboardRepository: LocalClipboardRepository,
    private val remoteClipboardRepository: RemoteClipboardRepository
) {
    suspend fun execute(
        isLogin: Boolean,
        onComplete: () -> Unit,
    ): Flow<List<ClipboardItem>> {


        return flow {
            onComplete()
            if (isLogin) {

                emitAll(
                    remoteClipboardRepository.getAll()
                        .map { entities -> entities.map { it.toModel() } })
            } else {
                emitAll(
                    localClipboardRepository.getAll()
                        .map { entities -> entities.map { it.toModel() } })

            }


        }
    }
}