package com.polaris.clipboard_list.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard_list.ClipboardListSeen
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import kotlinx.coroutines.flow.StateFlow

fun NavController.navigateClipboardList() {
    navigate(ClipboardList.route){
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.clipboardListNavGraph(
    clipboardItemList: StateFlow<List<ClipboardFolder>>,
    onEditClick:(item: ClipboardItem)->Unit,
    onAddFolderClick :()->Unit
) {
    composable(route = ClipboardList.route) {

        ClipboardListSeen(clipboardItemList.collectAsState().value, onEditClick = onEditClick,onAddFolderClick =onAddFolderClick)
    }
}
object ClipboardList {
    const val route = "clipboard_list"
}