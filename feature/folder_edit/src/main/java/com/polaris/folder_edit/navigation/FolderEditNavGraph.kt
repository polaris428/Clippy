package com.polaris.folder_edit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.folder_edit.FolderEditSeen
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import kotlinx.coroutines.flow.StateFlow

fun NavController.navigateFolderEdit() {
    navigate(ClipboardList.route){
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.folderEditNavGraph(
    onPostFolderSuccess:()->Unit,
    onPostFolderFile:()->Unit
) {
    composable(route = ClipboardList.route) {
        FolderEditSeen(onPostFolderSuccess,onPostFolderFile)

    }
}
object ClipboardList {
    const val route = "folder_edit"
}