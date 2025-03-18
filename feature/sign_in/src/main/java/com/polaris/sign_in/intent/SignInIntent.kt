package com.polaris.sign_in.intent

import com.polaris.model.dto.UserDTO
import com.polaris.model.model.User

sealed class SignInIntent {
    object PostInitFolderIntent:SignInIntent()
    data class GetCheckIfUserExists(val uid:String) :SignInIntent()
    data class PostUserInfoIntent(val user: User):SignInIntent()
    data class PostInitClipboardData(val folderId:String):SignInIntent()
    object GetClipboardData:SignInIntent()
}