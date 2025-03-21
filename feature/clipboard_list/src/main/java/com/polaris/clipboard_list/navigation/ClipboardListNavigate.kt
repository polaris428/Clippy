package com.polaris.clipboard_list.navigation

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard_list.ClipboardListSeen
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import com.polaris.util.toJson
import kotlinx.coroutines.flow.StateFlow

fun NavController.navigateClipboardList() {
    navigate(ClipboardList.route){
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.clipboardListNavGraph(
    clipboardItemList:List<ClipboardFolder>?,
    onEditClick:(item: ClipboardItem)->Unit,
    onAddFolderClick :()->Unit,
    onJoinFolderClick:()->Unit
) {
    composable(route = ClipboardList.route) {
        Log.e("poalris찐",clipboardItemList?.toJson()?:"dsadsf")
        ClipboardListSeen(clipboardItemList, onEditClick = onEditClick,onAddFolderClick =onAddFolderClick,onJoinFolderClick)
    }
}
object ClipboardList {
    const val route = "clipboard_list"
}