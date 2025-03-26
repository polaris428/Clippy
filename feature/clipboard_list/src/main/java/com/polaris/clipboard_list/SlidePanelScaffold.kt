package com.polaris.clipboard_list

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SlidePanelScaffold(
    modifier: Modifier = Modifier,
    panelWidth: Dp = 300.dp,
    panelContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val panelWidthPx = with(density) { 300.dp.toPx() }
    val rawDragOffset = remember { Animatable(-panelWidthPx) } // 초기 닫힘 상태
    var isPanelOpen by remember { mutableStateOf(false) }
    val velocityTracker = remember { VelocityTracker() }
    var isDragging by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isPanelOpen) 0.5f else 0f,
        animationSpec = tween(250, easing = FastOutSlowInEasing),
        label = "backgroundAlpha"
    )

    LaunchedEffect(isPanelOpen) {
        coroutineScope.launch {
            if (isPanelOpen) {
                rawDragOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            } else {
                rawDragOffset.animateTo(
                    targetValue = -panelWidthPx,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        velocityTracker.resetTracking()
                        isDragging = true
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            rawDragOffset.snapTo((rawDragOffset.value + dragAmount.x).coerceIn(-panelWidthPx, 0f))
                        }
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                    },
                    onDragEnd = {
                        isDragging = false
                        val velocity = velocityTracker.calculateVelocity().x
                        val threshold = panelWidthPx / 2

                        coroutineScope.launch {
                            if (velocity > 1000 || rawDragOffset.value > -threshold) {
                                isPanelOpen = true
                            } else {
                                isPanelOpen = false
                            }
                        }
                    }
                )
            }
    ) {
        content()

        if (isPanelOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            isPanelOpen = false
                            coroutineScope.launch {
                                rawDragOffset.animateTo(
                                    -panelWidthPx,
                                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                                )
                            }
                        }
                    )
                    .background(Color.Black.copy(alpha = backgroundAlpha))
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(panelWidth)
                .offset { IntOffset(rawDragOffset.value.roundToInt(), 0) }
        ) {
            panelContent()
        }
    }


}
