package com.polaris.main

import com.polaris.data.local.ClipboardItem

sealed class MainIntent {
    object getAllClipboardListIntent: MainIntent()
    data class postClipboarInsertIntent (val txext: String): MainIntent()
}