package com.polaris.data.repository.user

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.polaris.domin.repository.user.UserRepository
import com.polaris.model.dto.UserDTO
import com.polaris.util.PrefManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : UserRepository {

    private val userDatabase: DatabaseReference
        get() = firebaseDatabase.getReference("user")

    override suspend fun postUserInfo(userDTO: UserDTO): Flow<Boolean> {
        return try {

            userDatabase.child(PrefManager.userUid).setValue(userDTO).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

    override suspend fun getCheckIfUserExists(uid: String): Boolean {
        return try {
            val snapshot = userDatabase.child(uid).get().await()
            snapshot.exists() // 해당 UID가 존재하는지 확인
        } catch (e: Exception) {
            false
        }
    }

}