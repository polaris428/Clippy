package com.polaris.clipboard_edit.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard_edit.ClipboardEditSeen
import com.polaris.data.local.ClipboardItem
import kotlinx.coroutines.flow.StateFlow

fun NavController.navigateClipboardEdit() {
    navigate(ClipboardEditRoute.route){

    }
}

fun NavGraphBuilder.questionNavGraph(
    clipboardItem: StateFlow<ClipboardItem>
) {
    composable(route = ClipboardEditRoute.route) {
        val clipboardState by clipboardItem.collectAsState()
        ClipboardEditSeen(clipboardState)
    }
}
object ClipboardEditRoute {
    const val route = "clipboard_edit"
}