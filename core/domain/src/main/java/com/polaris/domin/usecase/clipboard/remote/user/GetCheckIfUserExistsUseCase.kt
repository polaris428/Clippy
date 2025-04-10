package com.polaris.domin.usecase.clipboard.remote.user

import com.polaris.domin.repository.user.UserRepository
import com.polaris.model.model.User
import com.polaris.model.model.toDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCheckIfUserExistsUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun execute(
        uid:String

    ): Flow<Boolean> {


        return flow {
            userRepository.getCheckIfUserExists(uid=uid)


        }
    }
}