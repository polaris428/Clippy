package com.polaris.data.repository


import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.model.dto.ClipboardFolderDTO
import com.polaris.model.dto.ClipboardItemDTO
import com.polaris.model.response.ClipboardFolderResponse
import com.polaris.util.PrefManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import java.util.Collections
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

internal class RemoteClipboardRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : RemoteClipboardRepository {


    private val folderDatabase: DatabaseReference
        get() = firebaseDatabase.getReference("folder")

    private val userDatabase: DatabaseReference
        get() = firebaseDatabase.getReference("user")

    override suspend fun postDataMigration(itemList: List<ClipboardItemDTO>): Flow<Boolean> {
        return try {
            itemList.reversed().forEach { item ->
                //   clipboardDatabase.push().setValue(item).await() // 개별적으로 push
            }
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }
    }

    override suspend fun insert(folderId: String, item: ClipboardItemDTO): Flow<Boolean> {
        return try {
            folderDatabase.child(folderId).child("clipboard_dateList").push().setValue(item).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun getAll(ids: List<String>): Flow<List<ClipboardFolderResponse>> =
        callbackFlow {
            if (ids.isEmpty()) {
                trySend(emptyList()).isSuccess
                close()
                return@callbackFlow
            }

            val resultList = Collections.synchronizedList(mutableListOf<ClipboardFolderResponse>())
            val listeners = mutableListOf<ValueEventListener>()
            val remainingCount = AtomicInteger(ids.size) // AtomicInteger로 동기화 처리

            for (id in ids) {
                val listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.getValue(ClipboardFolderResponse::class.java)?.let { item ->
                            resultList.add(item) // 동기화 리스트 사용
                        }

                        if (remainingCount.decrementAndGet() == 0) {
                            trySend(resultList.toList()).isSuccess
                            close() // 모든 데이터가 로드되었으면 한 번만 방출 후 종료
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // 특정 요청이 실패해도 진행 가능하도록 처리 (완전 실패로 간주하지 않음)
                        if (remainingCount.decrementAndGet() == 0) {
                            trySend(resultList.toList()).isSuccess
                            close()
                        }
                    }
                }

                folderDatabase.child(id).addListenerForSingleValueEvent(listener)
                listeners.add(listener)
            }

            awaitClose {
                for (listener in listeners) {
                    folderDatabase.removeEventListener(listener)
                }
            }
        }

    override suspend fun getClipboardFolder(id: String): Flow<ClipboardFolderResponse> =
        callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(ClipboardFolderResponse::class.java)?.let { item ->
                        trySend(item).isSuccess
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            folderDatabase.child(id).addValueEventListener(listener)
            awaitClose { folderDatabase.removeEventListener(listener) }
        }


    override suspend fun delete(itemId: Int): Flow<Boolean> {
        return try {
            // clipboardDatabase.child(itemId.toString()).removeValue().await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun updatePinStatus(itemId: Int, pinState: Boolean): Flow<Boolean> {
        return try {
            folderDatabase.child(itemId.toString()).child("pinned")
                .setValue(pinState).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }
    }

    override suspend fun updateClipboardItem(clipboardItem: ClipboardItemDTO): Flow<Boolean> {
        return try {
            folderDatabase.child(clipboardItem.timestamp.toString())
                .setValue(clipboardItem).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun clearAll(): Flow<Boolean> {
        return try {
            folderDatabase.removeValue().await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }
    }

    override suspend fun insertFolder(folder: ClipboardFolderDTO): Flow<Boolean> {
        return try {
            val folderId = FirebaseDatabase.getInstance().getReference("folders").push().key!!
            folder.id = folderId
            folderDatabase.child(folderId).setValue(folder).await()
            userDatabase.child(PrefManager.userUid).child("folder_list").push().setValue(folderId)
            PrefManager.folderIdList += listOf(folderId)
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun getFolderList(): Flow<List<ClipboardFolderResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun joinFolder(folderId: String): Flow<Boolean> = flow {
        try {
            val folderSnapshot = folderDatabase.child(folderId).get().await()
            if (!folderSnapshot.exists()) {
                Log.e("polaris040428","1.5")
                emit(false)
                return@flow
            }
            val folderData = folderSnapshot.getValue(ClipboardFolderResponse::class.java)

            if (folderData == null || !folderData.share) {
                emit(false)
                return@flow
            }

            // 현재 사용자 ID 가져오기
            val userId = PrefManager.userUid

            // 사용자의 folder_list에 추가 (코루틴 방식으로 변경)
            userDatabase.child(PrefManager.userUid).child("folder_list").push().setValue(folderId)

            // PrefManager 업데이트
            PrefManager.folderIdList += listOf(folderId)

            emit(true)
        } catch (e: Exception) {

            emit(false)
        }
    }

    override suspend fun upDateFolder() {


    }
}
