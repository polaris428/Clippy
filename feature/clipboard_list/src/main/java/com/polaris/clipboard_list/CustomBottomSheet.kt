package com.polaris.clipboard_list

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polaris.model.model.ClipboardItem
import kotlinx.coroutines.launch
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.bottomSheetTextColor


@Preview(showBackground = true)
@Composable
fun CustomBottomSheetPreview() {
    val isPreview = LocalInspectionMode.current // ✅ 프리뷰 모드 감지
    CustomBottomSheet(true , item = ClipboardItem(),isPreview= isPreview)
}

@Composable
fun CustomBottomSheet(
    isOpen: Boolean,
    item: ClipboardItem?,
    onCopy: (ClipboardItem) -> Unit = {},
    onEdit: (ClipboardItem) -> Unit = {},
    onPin: (ClipboardItem) -> Unit = {},
    onShare: (ClipboardItem) -> Unit = {},
    onDelete: (ClipboardItem) -> Unit = {},
    onDismissEvent: () -> Unit = {},
    onDismiss: () -> Unit ={},
    isPreview:Boolean = false
) {


    if (!isOpen || item == null) return
    val pinIcon = if (item.isPinned) R.drawable.ic_pin_off else R.drawable.ic_pin_on
    var isVisible by remember { mutableStateOf(isPreview) }
    val coroutineScope = rememberCoroutineScope()

    val animOffset = remember { Animatable(if (isPreview) 0f else 500f) } // ✅ 프리뷰에서는 바로 표시

    LaunchedEffect(Unit) {
        isVisible = true
        animOffset.animateTo(0f, tween(500, easing = FastOutSlowInEasing))
    }

    BackHandler(isVisible) {
        coroutineScope.launch {
            onDismissEvent()
            animOffset.animateTo(500f, tween(300, easing = FastOutSlowInEasing))
            isVisible = false
            onDismiss()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = animOffset.value.dp) // ✅ 애니메이션 적용 (프리뷰에서는 0)
                .background(Color.White), verticalAlignment = Alignment.CenterVertically
        ) {
            SheetOption("복사", R.drawable.ic_content_paste, Modifier.weight(1f)) {

                onCopy(item)
                onDismissEvent()
                coroutineScope.launch {
                    animOffset.animateTo(
                        500f, animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                    isVisible = false
                    onDismiss()

                }

            }
            SheetOption("수정", R.drawable.ic_edit, Modifier.weight(1f)) {
                onEdit(item)
                onDismissEvent()
                onDismiss()
            }
            SheetOption("핀", pinIcon, Modifier.weight(1f)) {
                onPin(item)
                onDismissEvent()
                coroutineScope.launch {
                    animOffset.animateTo(
                        500f, animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                    isVisible = false
                    onDismiss()
                }
            }
            SheetOption("공유", R.drawable.ic_share, Modifier.weight(1f)) {
                onShare(item)
                onDismissEvent()
                coroutineScope.launch {
                    animOffset.animateTo(
                        500f, animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                    isVisible = false
                    onDismiss()
                }
            }
            SheetOption("삭제", R.drawable.ic_delete, Modifier.weight(1f)) {
                onDelete(item)
                onDismissEvent()
                coroutineScope.launch {
                    animOffset.animateTo(
                        500f, animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                    isVisible = false
                    onDismiss()
                }
            }
        }
    }
}

@Composable
fun SheetOption(
    text: String, @DrawableRes imageId: Int, modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .clickable() { onClick() }
        .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = bottomSheetTextColor,
            textAlign = TextAlign.Center
        )
    }
}

