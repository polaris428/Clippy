package com.polaris.data.repository


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.polaris.model.ClipboardItem
import com.polaris.domin.repository.RemoteClipboardRepository
import com.polaris.util.PrefManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class RemoteClipboardRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : RemoteClipboardRepository {
    private val database: DatabaseReference
        get() = firebaseDatabase.getReference(PrefManager.userUid).child("clipboard")


    override suspend fun postDataMigration(itemList: List<ClipboardItem>): Flow<Boolean> {
        return try {
            itemList.reversed().forEach { item ->
                database.push().setValue(item).await() // 개별적으로 push
            }
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }
    }

    override suspend fun insert(item: ClipboardItem): Flow<Boolean> {
        return try {
            database.push().setValue(item).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun getAll(): Flow<List<ClipboardItem>> = callbackFlow {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { it.getValue(ClipboardItem::class.java) }
                trySend(items).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        database.addValueEventListener(listener)
        awaitClose { database.removeEventListener(listener) } // 스트림 종료 시 리스너 제거
    }


    override suspend fun delete(itemId: Int): Flow<Boolean> {
        return try {
            database.child(itemId.toString()).removeValue().await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun updatePinStatus(itemId: Int, pinState: Boolean): Flow<Boolean> {
        return try {
            database.child(itemId.toString()).child("pinned")
                .setValue(pinState).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }
    }

    override suspend fun updateClipboardItem(clipboardItem:ClipboardItem): Flow<Boolean> {
        return try {
        database.child(clipboardItem.id.toString())
                .setValue(clipboardItem).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun clearAll(): Flow<Boolean> {
        return try {
            database.removeValue().await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }
    }
}
