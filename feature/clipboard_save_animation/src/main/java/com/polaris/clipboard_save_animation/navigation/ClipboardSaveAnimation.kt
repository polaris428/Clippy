package com.polaris.clipboard_save_animation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.clipboard_save_animation.SaveAnimationSeen


fun NavController.navigateClipboardSaveAnimation() {
    navigate(ClipboardRoute.route){

    }
}

fun NavGraphBuilder.clipboardSaveAnimation(
    afterAnimation:()->Unit
) {
    composable(route = ClipboardRoute.route) {
        SaveAnimationSeen(afterAnimation)
    }
}

object ClipboardRoute {
    const val route = "clipboard_save_animation"
}