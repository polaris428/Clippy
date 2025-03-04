package com.polaris.clipboard_edit.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard_edit.ClipboardEditSeen
import com.polaris.data.local.ClipboardItem
import com.polaris.shared.MainViewModel
import kotlinx.coroutines.flow.StateFlow

fun NavController.navigateClipboardEdit() {
    navigate(ClipboardEditRoute.route){

    }
}

fun NavGraphBuilder.questionNavGraph(
    mainViewModel: MainViewModel,
    onSaveClick:()->Unit = {},
) {
    composable(route = ClipboardEditRoute.route) {

        ClipboardEditSeen(mainViewModel,onSaveClick = onSaveClick)
    }
}
object ClipboardEditRoute {
    const val route = "clipboard_edit"
}