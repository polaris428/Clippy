package com.polaris.clipboard_list.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard_list.ClipboardListSeen
import com.polaris.data.local.ClipboardItem
import kotlinx.coroutines.flow.StateFlow

fun NavController.navigateClipboardList() {
    navigate(ClipboardList.route){

    }
}

fun NavGraphBuilder.clipboardListNavGraph(
    clipboardItemList: StateFlow<List<ClipboardItem>>,

) {
    composable(route = ClipboardList.route) {

        ClipboardListSeen(clipboardItemList.collectAsState().value)
    }
}
object ClipboardList {
    const val route = "clipboard_edit"
}