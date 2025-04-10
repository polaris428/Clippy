package com.polaris.clipboard_save_animation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard_save_animation.SaveAnimationSeen


fun NavController.navigateClipboardSaveAnimation() {
    navigate(ClipboardSaveAnimationRoute.route){
        launchSingleTop = true
    }
}

fun NavGraphBuilder.clipboardSaveAnimation(
    afterAnimation:()->Unit
) {
    composable(route = ClipboardSaveAnimationRoute.route) {
        SaveAnimationSeen(afterAnimation)
    }
}

object ClipboardSaveAnimationRoute {
    const val route = "clipboard_save_animation"
}