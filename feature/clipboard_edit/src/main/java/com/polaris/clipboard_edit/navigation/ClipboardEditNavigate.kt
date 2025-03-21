package com.polaris.clipboard_edit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard_edit.ClipboardEditSeen
import com.polaris.shared.MainViewModel

fun NavController.navigateClipboardEdit() {
    navigate(ClipboardEditRoute.route){

    }
}

fun NavGraphBuilder.clipboardEdit(

    onSaveClick: (type: String, title: String) -> Unit = { _, _ -> },
) {
    composable(route = ClipboardEditRoute.route) {

       // ClipboardEditSeen(mainViewModel,onSaveClick = onSaveClick)
    }
}
object ClipboardEditRoute {
    const val route = "clipboard_edit"
}