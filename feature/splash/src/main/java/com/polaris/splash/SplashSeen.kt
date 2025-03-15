package com.polaris.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSButton
import com.polaris.designsystem.ui.theme.CDSColumn
import com.polaris.designsystem.ui.theme.Gray40
import com.polaris.shared.MainViewModel
import com.polaris.shared.intent.MainIntent
import com.polaris.splash.intent.SplashIntent
import com.polaris.util.PrefManager
import kotlinx.coroutines.delay

@Composable
fun SplashSeen(viewModel: MainViewModel, splashViewModel: SplashViewModel= hiltViewModel(), onSplashCompleted:()->Unit){

    val clipboardItem =  viewModel.clipboardItem.collectAsState()

    viewModel.processIntent(MainIntent.getAllClipboardListIntent)
    LaunchedEffect(clipboardItem.value) {
        delay(2000) // 3초 딜레이
        onSplashCompleted()
    }

    if (!PrefManager.userSignInSkip) {
        splashViewModel.processIntent(SplashIntent.initPostFolder())
    }
    SplashView()

}
@Composable
@Preview(showBackground = true)
fun SplashView(){
    CDSColumn(horizontalAlignment = Alignment.CenterHorizontally){
        Row(Modifier.weight(1f)) {
            Column(modifier = Modifier.fillMaxSize(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Clippy",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    "복사 그 이상, 더 스마트한 클립보드.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF333333)
                )

            }
        }


    }
}