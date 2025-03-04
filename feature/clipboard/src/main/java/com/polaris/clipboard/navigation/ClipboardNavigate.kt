package com.polaris.clipboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard.ClipboardSeen
import com.polaris.clipboard.ClipboardViewModel


fun NavController.navigateClipboard() {
    navigate(ClipboardRoute.route){

    }
}

fun NavGraphBuilder.clipboardNavGraph(
    viewModel: ClipboardViewModel,
    onEditClick:()->Unit,
    onDismiss : () ->Unit
) {
    composable(route = ClipboardRoute.route) {
        ClipboardSeen(viewModel,onEditClick = onEditClick , onDismiss = onDismiss)
    }
}

object ClipboardRoute {
    const val route = "clipboard"
}