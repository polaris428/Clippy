package com.polaris.domin.repository.user

import com.polaris.model.dto.UserDTO
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun postUserInfo(userDTO: UserDTO):Flow<Boolean>
}