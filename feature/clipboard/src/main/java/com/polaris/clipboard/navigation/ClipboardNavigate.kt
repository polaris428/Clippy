package com.polaris.clipboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard.ClipboardSeen
import com.polaris.model.model.ClipboardItem


fun NavController.navigateClipboard() {
    navigate(ClipboardRoute.route){

    }
}

fun NavGraphBuilder.clipboardNavGraph(
    url: String,
    onSaveSuccess:()->Unit,
    onEditClick:(item: ClipboardItem)->Unit,
    onDismiss : () ->Unit
) {
    composable(route = ClipboardRoute.route) {
        ClipboardSeen(url,onSaveSuccess = onSaveSuccess,onEditClick = onEditClick , onDismiss = onDismiss)
    }
}

object ClipboardRoute {
    const val route = "clipboard"
}