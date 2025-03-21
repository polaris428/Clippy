package com.polaris.clipboard_list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.polaris.clipboard_folder.SlidePanel
import kotlinx.coroutines.launch
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSColumn
import com.polaris.designsystem.ui.theme.ClipboardItemView
import com.polaris.designsystem.ui.theme.bottomSheetTextColor
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import com.polaris.util.detectSwipe
import com.polaris.util.getTodayStartTimestamp
import com.polaris.util.getYearMonth
import kotlin.math.roundToInt

@Composable
fun ClipboardListSeen(
    clipboardFolder: List<ClipboardFolder>?=null,
    onEditClick: (item: ClipboardItem) -> Unit,
    onAddFolderClick:()->Unit,
    onJoinFolderClick:()->Unit
) {
    val viewModel: ClipboardListViewModel = hiltViewModel()
    val isSheetOpen by viewModel.isSheetOpen.collectAsState()
    val selectedClipboardItem by viewModel.selectedClipboardItem.collectAsState()

    val onLongPressState = rememberUpdatedState(viewModel::processIntent)
    val onDeleteState = rememberUpdatedState(viewModel::processIntent)
    val onShearState = rememberUpdatedState(viewModel::processIntent)
    val onDismissState = rememberUpdatedState(viewModel::processIntent)

    var isPanelOpen by remember { mutableStateOf(false) }
    val index by viewModel.index.collectAsState()
    val context = LocalContext.current
    Log.e("poalris040428",clipboardFolder.toString())
    if (clipboardFolder.isNullOrEmpty()  ) {
        EmptyListView()
    } else {
        val density = LocalDensity.current
        val panelWidthPx = with(density) { 300.dp.toPx().roundToInt() } // 패널 너비 px 변환
        var isPanelOpen by remember { mutableStateOf(false) }
        var rawDragOffset by remember { mutableStateOf(if (isPanelOpen) 0f else -panelWidthPx.toFloat()) }
        val velocityTracker = remember { VelocityTracker() }
        var isDragging by remember { mutableStateOf(false) } // 드래그 중 여부 체크

        val backgroundAlpha by animateFloatAsState(
            targetValue = if (isPanelOpen) 0.5f else 0f,
            animationSpec = tween(250, easing = FastOutSlowInEasing),
            label = "backgroundAlpha"
        )
        Box(modifier = Modifier
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
            }) {
            ClipboardView(clipboardFolder[index].clipboardDateList,
                selectedClipboardItem,
                !isPanelOpen,
                onLongPress = {
                    if (!isPanelOpen)
                        onLongPressState.value(ClipboardListIntent.ItemLongPressed(it))
                    //viewModel.processIntent()
                },
                onClick = {

                    if (!isPanelOpen)
                        openUrl(context, it)
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
                onPin = { item ->
                    viewModel.processIntent(
                        ClipboardListIntent.UpdatePinClipboardDeleteIntent(
                            timestamp = item.timestamp,
                            pinState = item.isPinned
                        )
                    )
                },
                onDelete = { item ->
                    onDeleteState.value(ClipboardListIntent.postClipboardDeleteIntent(item.timestamp))

                },
                onDismissEvent = {
                    viewModel.clearSelectedClipboard()
                },
                onDismiss = {
                    onDismissState.value(ClipboardListIntent.BottomSheetDismissed)
                })

            if (isPanelOpen) {
                Box(modifier = Modifier.fillMaxSize(1f)
                    .clickable(
                        onClick = {
                            isPanelOpen = false
                            rawDragOffset = -panelWidthPx.toFloat()

                        },
                        indication = null, // ✅ 클릭 이펙트 제거
                        interactionSource = remember { MutableInteractionSource() } // ✅ 불필요한 효과 방지
                    )
                    .background(Color.Black.copy(alpha = backgroundAlpha))) { }
            }
            // ✅ 왼쪽에서 등장하는 슬라이드 패널
            SlidePanel(
                onItemClick = { viewModel.indexUpdate(index) },
                onAddFolderClick= { onAddFolderClick() },
                folderList = clipboardFolder,
                rawDragOffset = rawDragOffset,
                isDragging = isDragging,
                onJoinFolderClick = onJoinFolderClick

                )


        }
    }


}

@Composable
@Preview(showBackground = true)
fun EmptyListView() {
    CDSColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()

        Text("추가된 클립이 없어요")
    }
}

@Composable
fun CustomBottomSheetView(
    isSheetOpen: Boolean,
    selectedClipboardItem: ClipboardItem,
    onContent: (item: ClipboardItem) -> Unit = {},
    onEdit: (item: ClipboardItem) -> Unit = {},
    onShear: (item: ClipboardItem) -> Unit = {},
    onPin: (item: ClipboardItem) -> Unit = {},
    onDelete: (item: ClipboardItem) -> Unit = {},
    onDismissEvent: () -> Unit,
    onDismiss: () -> Unit,
) {

    if (isSheetOpen) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
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
    selectedClipboardItem: ClipboardItem? = null,
    clickable: Boolean = true,
    onClick: (url: String) -> Unit = {},
    onLongPress: (item: ClipboardItem) -> Unit = {},
) {
    val todayStartTimestamp = getTodayStartTimestamp()

    // ✅ Pinned 아이템을 먼저 가져옴
    val pinnedItems =
        clipboardItemList?.filter { it.isPinned }?.sortedByDescending { it.timestamp }.orEmpty()

    // ✅ 나머지 아이템을 시간순으로 정렬
    val otherItems =
        clipboardItemList?.filter { !it.isPinned }?.sortedByDescending { it.timestamp }.orEmpty()

    val todayItems = otherItems.filter { it.timestamp >= todayStartTimestamp }
    val previousItems = otherItems.filter { it.timestamp < todayStartTimestamp }


    // 📌 월 단위로 그룹화
    val groupedByMonth = previousItems.groupBy { getYearMonth(it.timestamp) }
    val context = LocalContext.current

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


                    ClipboardItemView(
                        clipboardItem = item,
                        selectedClipboardItem = selectedClipboardItem,
                        clickable = clickable,
                        onClick = { openUrl(context = context, url = item.url) },
                        onLongPress = { onLongPress(item) })
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
                    ClipboardItemView(
                        clipboardItem = item,
                        clickable = clickable,
                        selectedClipboardItem = selectedClipboardItem,
                        onClick = {
                            onClick(item.url!!)
                            //  openUrl(context = context, url = item.url)

                        },
                        onLongPress = { onLongPress(item) })
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
                    ClipboardItemView(
                        clickable = clickable,
                        clipboardItem = item,
                        selectedClipboardItem = selectedClipboardItem,
                        onClick = { openUrl(context = context, url = item.url) },
                        onLongPress = { onLongPress(item) })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

val dummyData = ClipboardItem(
    type = "GitHub",
    url = "https://www.github.com",
    title = "안드로이드 라이브러리 모음",
    faviconUrl = "https://github.githubassets.com/favicon.ico",
    timestamp = System.currentTimeMillis()
)

@Preview(showBackground = true)
@Composable
fun preView() {
    ClipboardItemView(
        clipboardItem = dummyData,
        selectedClipboardItem = dummyData,
        onLongPress = {})
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

    val context = LocalContext.current

    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .crossfade(true)  // 부드러운 이미지 전환
        .error(R.drawable.ic_logo)  // 오류 발생 시 기본 이미지
        //  .placeholder(R.drawable.ic_logo) // 로딩 중 기본 이미지
        .build()


    val painter = if (LocalInspectionMode.current) {
        // 프리뷰 모드에서는 Image와 painterResource 사용
        painterResource(id = R.drawable.ic_logo)
    } else {
        // 실제 모드에서는 rememberAsyncImagePainter 사용
        rememberAsyncImagePainter(model = imageRequest)
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
    onPin: (item: ClipboardItem) -> Unit = {},
    onShear: (item: ClipboardItem) -> Unit = {},
    onDelete: (item: ClipboardItem) -> Unit = {},
    onDismissEvent: () -> Unit = {},
    onDismiss: () -> Unit,
    isPreview: Boolean = false
) {

    var isVisible by remember { mutableStateOf(isPreview) }
    val coroutineScope = rememberCoroutineScope()

    val animOffset = remember { Animatable(if (isPreview) 0f else 500f) } // ✅ 프리뷰에서는 바로 표시

    var pinIcon =
        if (selectedClipboardItem.isPinned) R.drawable.ic_pin_off else R.drawable.ic_pin_on

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