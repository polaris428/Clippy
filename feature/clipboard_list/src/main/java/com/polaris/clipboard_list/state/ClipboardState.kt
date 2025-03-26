package com.polaris.clipboard_list.state

import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem

data class ClipboardState(
    var folders: List<ClipboardFolder> = emptyList(),
    val currentIndex: Int = 0,

    val isSheetOpen: Boolean = false,
    val selectedItem: ClipboardItem? = null
)
