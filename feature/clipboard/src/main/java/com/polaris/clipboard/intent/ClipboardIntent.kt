package com.polaris.clipboard.intent

sealed class ClipboardIntent {
    data class postClipboarInsertIntent (val txext: String): ClipboardIntent()
}