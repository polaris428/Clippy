package com.polaris.clipboard_save_animation.intent

import com.polaris.model.ClipboardItem

sealed class SaveAnimationSeenIntent {
    data class postClipboarInsertIntent (val clipboardItem: com.polaris.model.ClipboardItem): SaveAnimationSeenIntent()
}