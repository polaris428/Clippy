package com.polaris.folder_edit.state

sealed class FolderEditState {
    object Initialize:FolderEditState()
    object PostFolderLoading:FolderEditState()
    object PostFolderSuccess:FolderEditState()
    object PostFolderFail:FolderEditState()
}