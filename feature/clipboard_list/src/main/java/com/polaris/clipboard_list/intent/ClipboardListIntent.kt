package com.polaris.clipboard_list.intent

import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem


sealed class ClipboardListIntent {
    data class LoadInitialFolders(val folders: List<ClipboardFolder>) : ClipboardListIntent()
    data class ItemClicked(val item: ClipboardItem) : ClipboardListIntent()
    data class ItemLongPressed(val item: ClipboardItem) : ClipboardListIntent()
    data class Delete(  val folderId: String, val itemId:String,) : ClipboardListIntent()
    data class TogglePin(val folderId:String,val itemtId: String, val currentPin: Boolean) : ClipboardListIntent()
    data class IndexUpdate(val index: String) : ClipboardListIntent()
    object BottomSheetDismissed : ClipboardListIntent()

    object ClearSelection : ClipboardListIntent()
}