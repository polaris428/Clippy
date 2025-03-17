package com.polaris.clipboard_save_animation.intent

import com.polaris.model.model.ClipboardItem


sealed class SaveAnimationSeenIntent {
    data class postClipboarInsertIntent (val clipboardItem: ClipboardItem): SaveAnimationSeenIntent()
}