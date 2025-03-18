package com.polaris.domin.usecase.clipboard.user

import com.polaris.domin.repository.user.UserRepository
import com.polaris.model.dto.UserDTO
import com.polaris.model.model.User
import com.polaris.model.model.toDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostUserInfoUseCase  @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun execute(
        user: User

        ): Flow<Boolean> {


        return flow {

            emitAll(userRepository.postUserInfo(user.toDTO()))


        }
    }
}