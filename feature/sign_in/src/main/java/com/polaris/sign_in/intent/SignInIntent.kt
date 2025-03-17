package com.polaris.sign_in.intent

import com.polaris.model.dto.UserDTO

sealed class SignInIntent {
    object PostInitFolderIntent:SignInIntent()
    data class PostUserInfoIntent(val userDTO: UserDTO):SignInIntent()
}