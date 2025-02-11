package com.polaris.clipboard_list

import com.polaris.data.local.ClipboardItem

sealed class ClipboardListIntent {
    object getAllClipboardListIntent: ClipboardListIntent()
    data class postClipboarInsertIntent (val clipboardItem: ClipboardItem): ClipboardListIntent()
}