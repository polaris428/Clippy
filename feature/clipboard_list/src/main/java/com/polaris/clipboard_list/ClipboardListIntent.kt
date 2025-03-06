package com.polaris.clipboard_list

import com.polaris.model.ClipboardItem

sealed class ClipboardListIntent {
    data class ItemLongPressed(val item: com.polaris.model.ClipboardItem) : ClipboardListIntent()
    data class postClipboardDeleteIntent(val id:Int): ClipboardListIntent()
    data class UpdatePinClipboardDeleteIntent(val id:Int,val pinState:Boolean): ClipboardListIntent()
    object BottomSheetDismissed :ClipboardListIntent()
 //   data class postClipboarInsertIntent (val clipboardItem: ClipboardItem): ClipboardListIntent()

}