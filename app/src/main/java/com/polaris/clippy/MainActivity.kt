package com.polaris.clippy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.provider.Settings
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)

        if (!sharedText.isNullOrEmpty()) {
            // 클립보드에 저장
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Shared Text", sharedText)
            clipboard.setPrimaryClip(clip)
            Log.e("polaris428",sharedText)
            Toast.makeText(this, "텍스트가 클립보드에 복사되었습니다!", Toast.LENGTH_SHORT).show()
        }

      //  finish() // 화면을 닫고 백그라운드로 실행되도록 처리
    }



}