package com.polaris.clipboard_list

import com.polaris.data.local.ClipboardItem

sealed class ClipboardListIntent {
    data class ItemLongPressed(val item: ClipboardItem) : ClipboardListIntent()
    data class postClipboardDeleteIntent(val id:Int): ClipboardListIntent()
    data class UpdatePinClipboardDeleteIntent(val id:Int,val pinState:Boolean): ClipboardListIntent()
    object BottomSheetDismissed :ClipboardListIntent()
 //   data class postClipboarInsertIntent (val clipboardItem: ClipboardItem): ClipboardListIntent()

}