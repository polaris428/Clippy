package com.polaris.folder_join.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.folder_join.FolderJoinSeen


fun NavController.navigateFolderJoin() {
    navigate(ClipboardList.route)
}

fun NavGraphBuilder.folderJoinNavGraph(
    onPostFolderSuccess:()->Unit,
    onPostFolderFile:()->Unit
) {
    composable(route = ClipboardList.route) {
        FolderJoinSeen(onPostFolderSuccess,onPostFolderFile)

    }
}
object ClipboardList {
    const val route = "folder_join"
}