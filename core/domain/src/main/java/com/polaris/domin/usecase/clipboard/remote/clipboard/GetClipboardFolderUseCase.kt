package com.polaris.domin.usecase.clipboard.remote.clipboard

import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import com.polaris.model.response.ClipboardFolderResponse
import com.polaris.model.response.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetClipboardFolderUseCase @Inject constructor(
    private val remoteClipboardRepository: RemoteClipboardRepository
) {
    suspend fun execute(
        folderId: String,
    ): Flow<ClipboardFolder> {


        return flow {

            emitAll(remoteClipboardRepository.getClipboardFolder(folderId).map { it.toModel() })


        }
    }
}