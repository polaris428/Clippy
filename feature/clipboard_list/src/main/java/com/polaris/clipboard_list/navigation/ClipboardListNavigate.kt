package com.polaris.clipboard_list.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard_list.ClipboardListSeen
import com.polaris.model.ClipboardItem
import kotlinx.coroutines.flow.StateFlow

fun NavController.navigateClipboardList() {
    navigate(ClipboardList.route){
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.clipboardListNavGraph(
    clipboardItemList: StateFlow<List<com.polaris.model.ClipboardItem>>,
    onEditClick:(item: com.polaris.model.ClipboardItem)->Unit

) {
    composable(route = ClipboardList.route) {

        ClipboardListSeen(clipboardItemList.collectAsState().value, onEditClick = onEditClick)
    }
}
object ClipboardList {
    const val route = "clipboard_list"
}