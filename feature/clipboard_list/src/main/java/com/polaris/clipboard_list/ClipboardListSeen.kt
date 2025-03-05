package com.polaris.clipboard_list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.polaris.data.local.ClipboardItem
import com.polaris.designsystem.ui.theme.Gray50
import com.polaris.designsystem.ui.theme.LineView
import com.polaris.util.convertTimestampToMonthDay
import kotlinx.coroutines.launch
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.AnimatedCheckmarkWithCircle
import com.polaris.designsystem.ui.theme.bottomSheetTextColor
import com.polaris.util.getTodayStartTimestamp
import com.polaris.util.getYearMonth

@Composable
fun ClipboardListSeen(clipboardItemList: List<ClipboardItem>?,  onEditClick:(item:ClipboardItem)->Unit) {
    val viewModel: ClipboardListViewModel = hiltViewModel()
    val isSheetOpen by viewModel.isSheetOpen.collectAsState()
    val selectedClipboardItem by viewModel.selectedClipboardItem.collectAsState()

    val onLongPressState = rememberUpdatedState(viewModel::processIntent)
    val onDeleteState = rememberUpdatedState(viewModel::processIntent)
    val onShearState = rememberUpdatedState(viewModel::processIntent)
    val onDismissState = rememberUpdatedState(viewModel::processIntent)




    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        ClipboardView(clipboardItemList,selectedClipboardItem,
            onLongPress = {
                onLongPressState.value(ClipboardListIntent.ItemLongPressed(it))
               //viewModel.processIntent()
            }
        )

        CustomBottomSheetView(
            isSheetOpen = isSheetOpen,
            selectedClipboardItem = selectedClipboardItem,
            onContent = { item ->
                val textToCopy = item.url ?: item.title
                copyToClipboard(context, textToCopy)

            },
            onEdit = { item ->
                onEditClick(item)
            },
            onShear = { item ->

                val textToShare = item.url ?: item.title
                shareText(context, textToShare)
            },
            onPin = {  item ->
                viewModel.processIntent(ClipboardListIntent.UpdatePinClipboardDeleteIntent(id =item.id, pinState = item.isPinned ))
            },
            onDelete = { item ->
                onDeleteState.value(ClipboardListIntent.postClipboardDeleteIntent(item.id))

            },
            onDismissEvent = {
                viewModel.clearSelectedClipboard()
            },
            onDismiss = {
                onDismissState.value(ClipboardListIntent.BottomSheetDismissed)
            })


    }


}

@Composable
fun CustomBottomSheetView(
    isSheetOpen: Boolean,
    selectedClipboardItem: ClipboardItem,
    onContent: (item: ClipboardItem) -> Unit = {},
    onEdit: (item: ClipboardItem) -> Unit = {},
    onShear: (item: ClipboardItem) -> Unit = {},
    onPin:(item:ClipboardItem) -> Unit ={},
    onDelete: (item: ClipboardItem) -> Unit = {},
    onDismissEvent:()->Unit,
    onDismiss: () -> Unit,
) {

    if (isSheetOpen) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.Transparent),
            verticalArrangement = Arrangement.Bottom // ✅ 바텀 시트를 하단 정렬
        ) {
            CustomBottomSheet(selectedClipboardItem = selectedClipboardItem,
                onContent = {
                    onContent(it)
                }, onEdit = {
                    onEdit(it)
                }, onShear = {
                    onShear(it)
                }, onPin = {
                    onPin(it)

                }, onDelete = {
                    onDelete(it)
                },
                onDismissEvent = {
                    onDismissEvent()
                },
                onDismiss = {
                    onDismiss()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClipboardView(
    clipboardItemList: List<ClipboardItem>? = listOf(dummyData),
    selectedClipboardItem: ClipboardItem?= null,
    onLongPress: (item: ClipboardItem) -> Unit = {},
) {
    val todayStartTimestamp = getTodayStartTimestamp()

    // ✅ Pinned 아이템을 먼저 가져옴
    val pinnedItems = clipboardItemList?.filter { it.isPinned }?.sortedByDescending { it.timestamp }.orEmpty()

    // ✅ 나머지 아이템을 시간순으로 정렬
    val otherItems = clipboardItemList?.filter { !it.isPinned }?.sortedByDescending { it.timestamp }.orEmpty()

    val todayItems = otherItems.filter { it.timestamp >= todayStartTimestamp }
    val previousItems = otherItems.filter { it.timestamp < todayStartTimestamp }


    // 📌 월 단위로 그룹화
    val groupedByMonth = previousItems.groupBy { getYearMonth(it.timestamp) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Header()

        // ✅ Pinned 아이템 표시
        if (pinnedItems.isNotEmpty()) {
            Text(
                "Pinned",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                pinnedItems.forEach { item ->

                    ClipboardItemView(item,selectedClipboardItem) { onLongPress(item) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ✅ 오늘 데이터 표시
        if (todayItems.isNotEmpty()) {
            Text(
                "Today",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                todayItems.forEach { item ->
                    ClipboardItemView(item,selectedClipboardItem) { onLongPress(item) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ✅ 월별 데이터 표시
        groupedByMonth.forEach { (month, items) ->
            Text(
                text = month,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                items.forEach { item ->
                    ClipboardItemView(item,selectedClipboardItem) { onLongPress(item) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

val dummyData = ClipboardItem(
    id = -1,
    type = "GitHub",
    url = "https://www.github.com",
    title = "안드로이드 라이브러리 모음",
    faviconUrl = "https://github.githubassets.com/favicon.ico",
    timestamp = System.currentTimeMillis()
)

@Preview(showBackground = true)
@Composable
fun preView() {
    ClipboardItemView(dummyData, dummyData,onLongPress = {})
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClipboardItemView(clipboardItem: ClipboardItem, selectedClipboardItem: ClipboardItem?,onLongPress: () -> Unit) {
    val context = LocalContext.current


    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { openUrl(context, clipboardItem.url) },
                    onLongClick = {

                        onLongPress() // ✅ 기존 롱클릭 이벤트 실행
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
                            text = " | ", color = Gray50, fontSize = 14.sp
                        )
                        Text(
                            text = clipboardItem.type, color = Gray50, fontSize = 12.sp
                        )
                    }
                }
                LineView()
            }
        }

        // ✅ isSelected 값이 true일 때만 보이도록 설정
        AnimatedCheckmarkWithCircle(selectedClipboardItem?.id == clipboardItem.id)
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

        color = Color.White, modifier = Modifier.size(48.dp)


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
    CustomBottomSheet(dummyData, onDismiss = {}, isPreview = isPreview)
}

@Composable
fun CustomBottomSheet(
    selectedClipboardItem: ClipboardItem,
    onContent: (item: ClipboardItem) -> Unit = {},
    onEdit: (item: ClipboardItem) -> Unit = {},
    onPin: (item:ClipboardItem) -> Unit = {},
    onShear: (item: ClipboardItem) -> Unit = {},
    onDelete: (item: ClipboardItem) -> Unit = {},
    onDismissEvent :()-> Unit ={},
    onDismiss: () -> Unit,
    isPreview: Boolean = false
) {

    var isVisible by remember { mutableStateOf(isPreview) }
    val coroutineScope = rememberCoroutineScope()

    val animOffset = remember { Animatable(if (isPreview) 0f else 500f) } // ✅ 프리뷰에서는 바로 표시

    var pinIcon = if(selectedClipboardItem.isPinned) R.drawable.ic_pin_off else R.drawable.ic_pin_on

    LaunchedEffect(Unit) {
        if (!isPreview) { // ✅ 프리뷰가 아닐 때만 애니메이션 실행
            isVisible = true
            coroutineScope.launch {
                animOffset.animateTo(
                    0f, animationSpec = tween(500, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    BackHandler(isVisible) {
        coroutineScope.launch {
            onDismissEvent()
            animOffset.animateTo(
                500f, animationSpec = tween(300, easing = FastOutSlowInEasing)
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
                .background(Color.White), verticalAlignment = Alignment.CenterVertically
        ) {
            SheetOption("복사", R.drawable.ic_content_paste, Modifier.weight(1f)) {

                onContent(selectedClipboardItem)
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
                onEdit(selectedClipboardItem)
                onDismissEvent()
                onDismiss()
            }
            SheetOption("핀", pinIcon, Modifier.weight(1f)) {
                onPin(selectedClipboardItem)
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
                onShear(selectedClipboardItem)
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
                onDelete(selectedClipboardItem)
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


fun openUrl(context: Context, url: String?) {
    if (!url.isNullOrBlank()) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            setPackage("com.android.chrome")
        }
        context.startActivity(intent)
    }
}



@Preview
@Composable
fun CheckpointCard() {
    Box(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(
                text = "카드 내용",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp
            )
        }

        // 체크 포인트 (아이콘, 점 등)
        Box(
            modifier = Modifier
                .offset(y = -7.dp, x = 0.dp) // 위치 조정
                .size(24.dp)
                .background(Color.Red, shape = CircleShape)
        )
    }
}