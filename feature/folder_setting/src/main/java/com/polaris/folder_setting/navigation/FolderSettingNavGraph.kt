package com.polaris.folder_setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.folder_setting.FolderSettingSeen

fun NavController.navigateFolderSetting() {
    navigate(ClipboardSetting.route)
}

fun NavGraphBuilder.folderSettingNavGraph(

) {
    composable(route = ClipboardSetting.route) {
        FolderSettingSeen()

    }
}
object ClipboardSetting {
    const val route = "folder_setting"
}