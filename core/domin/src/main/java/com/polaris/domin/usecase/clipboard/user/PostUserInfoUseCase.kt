package com.polaris.domin.usecase.clipboard.user

import com.polaris.domin.repository.user.UserRepository
import com.polaris.model.dto.UserDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostUserInfoUseCase  @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun execute(
        userDTO: UserDTO

        ): Flow<Boolean> {


        return flow {
            userRepository.postUserInfo(userDTO)


        }
    }
}