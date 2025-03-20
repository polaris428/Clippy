package com.polaris.folder_join.state

sealed class FolderJoinState {

    object Initialize : FolderJoinState()
    object PostFolderLoading : FolderJoinState()
    object PostFolderSuccess : FolderJoinState()
    data class PostFolderFail(val errorCode:Int) : FolderJoinState()

}