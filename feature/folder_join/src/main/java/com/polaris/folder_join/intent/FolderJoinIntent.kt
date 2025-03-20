package com.polaris.folder_join.intent

sealed class FolderJoinIntent {
    data class postFolderJoinIntent(val folderId: String) : FolderJoinIntent()
}