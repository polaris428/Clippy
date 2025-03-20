package com.polaris.folder_edit.intent

import com.polaris.model.model.ClipboardFolder

sealed class FolderEditIntent {
    data class postFolderIntent(val clipboardFolder: ClipboardFolder) : FolderEditIntent()
}