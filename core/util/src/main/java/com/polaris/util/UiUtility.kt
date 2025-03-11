package com.polaris.util

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.detectSwipe(
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    threshold: Float = 20f
): Modifier {
    return this.pointerInput(Unit) {
        detectHorizontalDragGestures { _, dragAmount ->
            when {
                dragAmount < -threshold -> onSwipeLeft?.invoke()  // ✅ 왼쪽 스와이프 → 패널 닫기
                dragAmount > threshold -> onSwipeRight?.invoke() // ✅ 오른쪽 스와이프 → 패널 열기
            }
        }
    }
}