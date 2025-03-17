package com.polaris.data.repository.user

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
        get() = firebaseDatabase.getReference("user").child(PrefManager.userUid)

    override suspend fun postUserInfo(userDTO: UserDTO): Flow<Boolean> {
        return try {
            userDatabase.push().setValue(userDTO).await()
            flowOf(true)
        } catch (e: Exception) {
            flowOf(false)
        }

    }

}