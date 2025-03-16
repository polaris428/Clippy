package com.polaris.sign_in.intent

sealed class SignInIntent {
    object postInitFolderIntent:SignInIntent()
}