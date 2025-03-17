package com.polaris.clipboard_list

import com.polaris.model.model.ClipboardItem


sealed class ClipboardListIntent {
    data class ItemLongPressed(val item: ClipboardItem) : ClipboardListIntent()
    data class postClipboardDeleteIntent(val timestamp:Long): ClipboardListIntent()
    data class UpdatePinClipboardDeleteIntent(val timestamp:Long,val pinState:Boolean): ClipboardListIntent()
    object BottomSheetDismissed :ClipboardListIntent()
 //   data class postClipboarInsertIntent (val clipboardItem: ClipboardItem): ClipboardListIntent()

}