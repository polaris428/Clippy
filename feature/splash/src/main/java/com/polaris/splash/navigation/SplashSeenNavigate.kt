package com.polaris.splash.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.model.model.ClipboardFolder
import com.polaris.shared.MainViewModel
import com.polaris.splash.SplashSeen


fun NavController.navigateSplash() {
    navigate(SplashRoute.route){

    }
}

fun NavGraphBuilder.splashNavGraph(

    onSplashCompleted: (List<ClipboardFolder>) -> Unit

) {
    composable(route = SplashRoute.route) {
        SplashSeen(onSplashCompleted =  onSplashCompleted)

    }
}

object SplashRoute {
    const val route = "splash"
}
