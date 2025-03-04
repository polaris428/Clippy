package com.polaris.clipboard_edit.intent

sealed class ClipboardEditIntent {
    data class postClipboarInsertIntent (val txext: String): ClipboardEditIntent()
}