package com.polaris.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.polaris.model.ClipboardItem
import com.polaris.domin.repository.RemoteClipboardRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class RemoteClipboardRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : RemoteClipboardRepository {
    override suspend fun insert(item: com.polaris.model.ClipboardItem): Flow<Boolean> {
        return try {
            database.getReference("clipboard").push().setValue(item).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun getAll(): Flow<List<com.polaris.model.ClipboardItem>> = callbackFlow {
        val ref = database.getReference("clipboard")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { it.getValue(com.polaris.model.ClipboardItem::class.java) }
                trySend(items).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) } // 스트림 종료 시 리스너 제거
    }


    override suspend fun delete(itemId: Int): Flow<Boolean> {
        return try {
            database.getReference("clipboard").child(itemId.toString()).removeValue().await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun updatePinStatus(itemId: Int, pinState: Boolean): Flow<Boolean> {
        return try {
            database.getReference("clipboard").child(itemId.toString()).child("pinned")
                .setValue(pinState).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }
    }

    override suspend fun updateClipboardItem(clipboardItem: com.polaris.model.ClipboardItem): Flow<Boolean> {
        return try {
            database.getReference("clipboard").child(clipboardItem.id.toString())
                .setValue(clipboardItem).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun clearAll(): Flow<Boolean> {
        return try {
            database.getReference("clipboard").removeValue().await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }
    }
}
