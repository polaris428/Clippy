package com.polaris.clipboard_save_animation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.polaris.clipboard_save_animation.intent.SaveAnimationSeenIntent
import com.polaris.data.local.ClipboardItem
import kotlin.system.exitProcess

@Composable
fun SaveAnimationSeen(afterAnimation:()->Unit){

    SaveAnimationView(afterAnimation)
}
@Composable
fun SaveAnimationView(afterAnimation:()->Unit){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lottie))
    val progress by animateLottieCompositionAsState(composition)


    // 애니메이션 종료 감지 후 앱 종료
    LaunchedEffect(progress) {
        if (progress == 1f) {

            afterAnimation()

        }
    }
    Column(
        modifier = Modifier.fillMaxSize(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(100.dp)
        )
    }
}
