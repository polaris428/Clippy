package com.polaris.clipboard

import android.app.Activity.CLIPBOARD_SERVICE
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import com.polaris.designsystem.ui.theme.ClippyTheme
import com.polaris.util.fetchWebTitle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun  ClipboardSeen(sharedText:String) {
   // val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)

    if (!sharedText.isNullOrEmpty()) {
        // 클립보드에 저장
     //   val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
      //  val clip = ClipData.newPlainText("Shared Text", sharedText)
     //   clipboard.setPrimaryClip(clip)



    }
    Column {
        LocalContext.current
    }


    //finish()

}