package com.polaris.clipboard

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.polaris.clipboard.intent.ClipboardIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class ClipboardActivity : AppCompatActivity() {
    private val viewModel: ClipboardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (!sharedText.isNullOrEmpty()) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Shared Text", sharedText)
            clipboard.setPrimaryClip(clip)

            viewModel.processIntent(ClipboardIntent.postClipboarInsertIntent(sharedText))
            Toast.makeText(this, "클리퍼가 잘 저장했어요", Toast.LENGTH_SHORT).show()

        }

        setContent {

            LottieAnimationAndExit(this)

        }
    }
}

@Composable
fun LottieAnimationAndExit(activity: Activity) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lottie))
    val progress by animateLottieCompositionAsState(composition)


    // 애니메이션 종료 감지 후 앱 종료
    LaunchedEffect(progress) {
        if (progress == 1f) {
            activity.finish()  // Activity 종료
            exitProcess(0)  // 프로세스 종료
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