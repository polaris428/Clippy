package com.polaris.domin.usecase.clipboard.remote.clipboard



import com.polaris.database.model.toEntity
import com.polaris.domin.repository.LocalClipboardRepository
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.dto.ClipboardItemDTO
import com.polaris.model.dto.toDTO
import com.polaris.model.model.ClipboardItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostClipboardInsertUseCase @Inject constructor(
   private val localClipboardRepository: LocalClipboardRepository,
    private val clipboardRepository: RemoteClipboardRepository
){
    suspend fun execute(
        folderId:String,
        item: ClipboardItem,
        ):Flow<Boolean> {


        return flow {

            emitAll( clipboardRepository.insert(folderId,item.toDTO()))


        }
    }
}