package com.polaris.clipboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard.ClipboardSeen


fun NavController.navigateClipboard() {
    navigate(ClipboardRoute.route){

    }
}

fun NavGraphBuilder.clipboardNavGraph(
    title: String,
    siteName: String,
    onSaveClick:()->Unit,
    onEditClick:()->Unit,
    onDismiss : () ->Unit
) {
    composable(route = ClipboardRoute.route) {
        ClipboardSeen(title,siteName,onSaveClick = onSaveClick,onEditClick = onEditClick , onDismiss = onDismiss)
    }
}

object ClipboardRoute {
    const val route = "clipboard"
}