package com.polaris.clipboard_list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.polaris.data.local.ClipboardItem
import com.polaris.designsystem.ui.theme.Gray20
import com.polaris.designsystem.ui.theme.Gray50
import com.polaris.designsystem.ui.theme.Gray60
import com.polaris.designsystem.ui.theme.Gray80
import com.polaris.designsystem.ui.theme.Gray90
import com.polaris.designsystem.ui.theme.LineView
import com.polaris.util.convertTimestampToMonthDay
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.bottomSheetTextColor

@Composable
fun ClipboardListSeen(clipboardItemList: List<ClipboardItem>?) {
    var isSheetOpen by remember { mutableStateOf(false) } // 바텀 시트 열림 상태 관리
    val coroutineScope = rememberCoroutineScope()
    var clipboardItem by remember { mutableStateOf<ClipboardItem>(dummyData) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Header()
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                if (!clipboardItemList.isNullOrEmpty()) {
                    clipboardItemList.forEach { item ->
                        ClipboardItemView(item) { // 📌 onLongPress 이벤트 전달
                            clipboardItem = item
                            coroutineScope.launch {
                                isSheetOpen = true // 롱프레스 시 바텀 시트 열기
                            }
                        }
                    }
                }
            }
        }

        if (isSheetOpen) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Bottom // ✅ 바텀 시트를 하단 정렬
            ) {
                CustomBottomSheet(item = clipboardItem,onDismiss = { isSheetOpen = false })
            }
        }


    }


}

val dummyData = ClipboardItem(
    id = 3,
    type = "GitHub",
    url = "https://www.github.com",
    title = "안드로이드 라이브러리 모음",
    faviconUrl = "https://github.githubassets.com/favicon.ico",
    timestamp = System.currentTimeMillis()
)

@Preview(showBackground = true)
@Composable
fun preView() {
    ClipboardItemView(dummyData, onLongPress = {})
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClipboardItemView(clipboardItem: ClipboardItem, onLongPress: () -> Unit) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (!clipboardItem.url.isNullOrBlank()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clipboardItem.url))
                        intent.setPackage("com.android.chrome")
                        context.startActivity(intent)
                    }
                },
                onLongClick = {
                    onLongPress()
                },
                indication = rememberRipple(
                    color = Color.Gray, // 리플 색상 설정
                    bounded = true // Row 크기 내에서만 리플 퍼지게 설정
                ),
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(top = 14.dp, start = 8.dp)
    ) {
        if (!clipboardItem.faviconUrl.isNullOrEmpty()) {
            displayImage(clipboardItem.faviconUrl!!)
        }

        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                text = clipboardItem.title,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!clipboardItem.url.isNullOrEmpty()) {
                    Text(
                        text = convertTimestampToMonthDay(clipboardItem.timestamp),
                        color = Gray50,
                        fontSize = 12.sp
                    )
                    Text(
                        text = " | ",
                        color = Gray50,
                        fontSize = 14.sp
                    )
                    Text(
                        text = clipboardItem.type,
                        color = Gray50,
                        fontSize = 12.sp
                    )
                }
            }
            LineView()
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Clippy",
            fontSize = 35.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = Color(0xFF333333)
        )
    }
}


@Preview
@Composable
fun displayImage(imageUrl: String = "") {
    val painter = if (LocalInspectionMode.current) {
        // 프리뷰 모드에서는 Image와 painterResource 사용
        painterResource(id = R.drawable.ic_logo)
    } else {
        // 실제 모드에서는 rememberAsyncImagePainter 사용
        rememberAsyncImagePainter(model = imageUrl)
    }
    Surface(
        shape = RoundedCornerShape(8.dp),

        color = Color.White,
        modifier = Modifier
            .size(48.dp)


    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }

}

@Preview(showBackground = true)
@Composable
fun CustomBottomSheetPreview() {
    val isPreview = LocalInspectionMode.current // ✅ 프리뷰 모드 감지
    CustomBottomSheet(dummyData,onDismiss = {}, isPreview = isPreview)
}

@Composable
fun CustomBottomSheet(item: ClipboardItem, onDismiss: () -> Unit, isPreview: Boolean = false) {
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(isPreview) }
    val coroutineScope = rememberCoroutineScope()

    val animOffset = remember { Animatable(if (isPreview) 0f else 500f) } // ✅ 프리뷰에서는 바로 표시

    LaunchedEffect(Unit) {
        if (!isPreview) { // ✅ 프리뷰가 아닐 때만 애니메이션 실행
            isVisible = true
            coroutineScope.launch {
                animOffset.animateTo(
                    0f,
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    BackHandler(isVisible) {
        coroutineScope.launch {
            animOffset.animateTo(
                500f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
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
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SheetOption("복사", R.drawable.ic_content_paste, Modifier.weight(1f)) {
                if (item.url != null) {
                    copyToClipboard(context, item.url.toString())
                } else {
                    copyToClipboard(context, item.title)
                }
            }
            SheetOption("수정", R.drawable.ic_edit, Modifier.weight(1f)) { }
            SheetOption("공유", R.drawable.ic_share, Modifier.weight(1f)) {
                if (item.url != null) {
                    shareText(context, item.url.toString())
                } else {
                    shareText(context, item.title)
                }
            }
            SheetOption("삭제", R.drawable.ic_delete, Modifier.weight(1f)) {
                coroutineScope.launch {
                    animOffset.animateTo(
                        500f,
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                    isVisible = false
                    onDismiss()
                }
            }
        }
    }
}

@Composable
fun SheetOption(text: String, @DrawableRes imageId: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)) // ✅ 사각형이지만 모서리를 둥글게
            .clickable(){ onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(4.dp)) // ✅ 아이콘과 텍스트 간 간격 추가
        Text(
            text = text,
            fontSize = 14.sp,
            color = bottomSheetTextColor,
            textAlign = TextAlign.Center
        )
    }
}


fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}
fun shareText(context: Context, text: String, title: String = "Share via") {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, title))
}


@Preview(showBackground = true)
@Composable
fun PreviewTest() {
    Column {

        ClipboardListSeen(listOf(dummyData, dummyData, dummyData, dummyData, dummyData))

    }


}