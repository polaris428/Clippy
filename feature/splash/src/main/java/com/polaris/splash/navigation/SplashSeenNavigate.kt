package com.polaris.splash.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.shared.MainViewModel
import com.polaris.splash.SplashSeen


fun NavController.navigateSplash() {
    navigate(SplashRoute.route){

    }
}

fun NavGraphBuilder.splashNavGraph(
    viewModel: MainViewModel,
    onSplashCompleted: () -> Unit

) {
    composable(route = SplashRoute.route) {
        SplashSeen(viewModel,onSplashCompleted)

    }
}

object SplashRoute {
    const val route = "splash"
}
