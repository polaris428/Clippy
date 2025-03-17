package com.polaris.sign_in.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.sign_in.SignInSeen
import com.polaris.sign_in.viewModel.SignInViewModel
import com.polaris.util.GoogleSignInHelper


fun NavController.navigateSignIn() {
    navigate(SignInRoute.route){

    }
}

fun NavGraphBuilder.signInNavGraph(
    onSignInClick:()->Unit,
    googleSignInHelper:GoogleSignInHelper,
    onSignIncomplete: ()->Unit={}

) {
    composable(route = SignInRoute.route) {
        SignInSeen(onSignInClick,googleSignInHelper,onSignIncomplete)

    }
}

object SignInRoute {
    const val route = "sign_in"
}
