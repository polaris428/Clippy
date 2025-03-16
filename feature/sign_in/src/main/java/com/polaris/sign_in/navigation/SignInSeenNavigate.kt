package com.polaris.sign_in.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.polaris.sign_in.SignInSeen
import com.polaris.sign_in.viewModel.SignInViewModel


fun NavController.navigateSignIn() {
    navigate(SignInRoute.route){

    }
}

fun NavGraphBuilder.signInNavGraph(
    onSignInClick:()->Unit,
    onSignInAnonymouslyClick:(viewModel:SignInViewModel)->Unit,
    onSignIncomplete: ()->Unit={}

) {
    composable(route = SignInRoute.route) {
        SignInSeen(onSignInClick,onSignInAnonymouslyClick,onSignIncomplete)

    }
}

object SignInRoute {
    const val route = "sign_in"
}
