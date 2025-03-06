package com.polaris.sign_in.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.sign_in.SignInSeen



fun NavController.navigateSignIn() {
    navigate(SignInRoute.route){

    }
}

fun NavGraphBuilder.signInNavGraph(
    onSignInClick:()->Unit

) {
    composable(route = SignInRoute.route) {
        SignInSeen(onSignInClick)

    }
}

object SignInRoute {
    const val route = "sign_in"
}
