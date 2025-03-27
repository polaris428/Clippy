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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SlidePanelScaffold(
    modifier: Modifier = Modifier,
    panelContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val panelWidth = screenWidth * 0.7f


    val density = LocalDensity.current
    val panelWidthPx = with(density) { panelWidth.toPx() }
    var isPanelOpen by remember { mutableStateOf(false) }
    var rawDragOffset by remember { mutableStateOf(if (isPanelOpen) 0f else -panelWidthPx.toFloat()) }

    val velocityTracker = remember { VelocityTracker() }
    var isDragging by remember { mutableStateOf(false) }

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isPanelOpen) 0.5f else 0f,
        animationSpec = tween(250, easing = FastOutSlowInEasing),
        label = "backgroundAlpha"
    )


    Box(
        modifier = modifier
            .fillMaxSize()


            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        velocityTracker.resetTracking()
                        isDragging = true // ✅ 드래그 시작 시 즉시 반영
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        rawDragOffset =
                            (rawDragOffset + dragAmount.x).coerceIn(-panelWidthPx.toFloat(), 0f)
                        velocityTracker.addPosition(change.uptimeMillis, change.position)

                    },
                    onDragEnd = {
                        isDragging = false // ✅ 드래그 종료 후 애니메이션 적용
                        val velocity = velocityTracker.calculateVelocity().x
                        val threshold = panelWidthPx / 2

                        rawDragOffset = if (velocity > 1000 || rawDragOffset > -threshold) {
                            isPanelOpen = true
                            0f
                        } else {
                            isPanelOpen = false
                            -panelWidthPx.toFloat()
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
                            rawDragOffset = -panelWidthPx.toFloat()
                        }
                    )
                    .background(Color.Black.copy(alpha = backgroundAlpha))
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(panelWidth)
                .offset { IntOffset(rawDragOffset.roundToInt(), 0) }
        ) {
            panelContent()
        }
    }


}
