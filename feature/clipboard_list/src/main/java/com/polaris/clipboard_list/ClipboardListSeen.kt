package com.polaris.clipboard_list

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
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

@Composable
fun ClipboardListSeen(clipboardItem: List<ClipboardItem>?) {
    var isSheetOpen by remember { mutableStateOf(false) } // 바텀 시트 열림 상태 관리
    val coroutineScope = rememberCoroutineScope()

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
                if (!clipboardItem.isNullOrEmpty()) {
                    clipboardItem.forEach { item ->
                        ClipboardItemView(item) { // 📌 onLongPress 이벤트 전달
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
                CustomBottomSheet(onDismiss = { isSheetOpen = false })
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

@Composable
fun CustomBottomSheet(onDismiss: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val animOffset = remember { Animatable(500f) } //  초기 위치는 500.dp 아래

    LaunchedEffect(Unit) {
        isVisible = true
        coroutineScope.launch {

        }
        animOffset.animateTo(0f, animationSpec = tween(500, easing = FastOutSlowInEasing)) // ✅ 부드러운 슬라이드 업 애니메이션
    }

    BackHandler(isVisible) {
        coroutineScope.launch {
            animOffset.animateTo(500f, animationSpec = tween(300, easing = FastOutSlowInEasing)) // ✅ 부드럽게 아래로 사라짐

            isVisible = false //  UI가 바로 사라지지 않도록 마지막에 변경
            onDismiss()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent) // 필요하면 배경 추가
    ) {
        Spacer(modifier = Modifier.weight(1f)) // 상단 공간을 차지하여 Row를 하단으로 밀어냄

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = animOffset.value.dp) // 애니메이션 적용
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SheetOption("복사") { /* 복사 기능 */ }
            SheetOption("수정") { /* 공유 기능 */ }
            SheetOption("공유") { /* 삭제 기능 */ }
            SheetOption("삭제") {
                coroutineScope.launch {
                    animOffset.animateTo(500f, animationSpec = tween(300, easing = FastOutSlowInEasing)) // ✅ 부드럽게 아래로 사라짐
                    isVisible = false // ✅ UI가 바로 사라지지 않도록 마지막에 변경
                    onDismiss()
                }
            }
        }
    }

}



@Composable
fun SheetOption(text: String, onClick: () -> Unit) { // ✅ 일반 람다로 변경
    Text(
        text = text,
        fontSize = 16.sp,
        color = Color.Blue,
        modifier = Modifier
            .clickable { onClick() } // ✅ 일반 람다 실행 가능

    )
}



@Preview(showBackground = true)
@Composable
fun PreviewTest() {
    Column {

        ClipboardListSeen(listOf(dummyData, dummyData, dummyData, dummyData, dummyData))

    }


}