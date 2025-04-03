package com.polaris.clipboard_list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.polaris.clipboard_folder.SlidePanel
import com.polaris.clipboard_list.intent.ClipboardListIntent
import com.polaris.clipboard_list.state.ClipboardState
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSColumn
import com.polaris.designsystem.ui.theme.ClipboardItemView
import com.polaris.designsystem.ui.theme.Header
import com.polaris.designsystem.ui.theme.dummyData
import com.polaris.model.model.ClipboardFolder
import com.polaris.model.model.ClipboardItem
import com.polaris.util.getTodayStartTimestamp
import com.polaris.util.getYearMonth
import com.polaris.util.toJson
import okhttp3.internal.http2.Header


@Composable
fun ClipboardListSeen(
    clipboardFolder: List<ClipboardFolder>? = null,
    onEditClick: (item: ClipboardItem) -> Unit,
    onAddFolderClick: () -> Unit,
    onJoinFolderClick: () -> Unit,
    onShareClick: (ClipboardFolder) -> Unit
) {

    val viewModel: ClipboardListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val folderList = viewModel.folderList.collectAsState()
    val currentList = folderList.value.getOrNull(uiState.currentIndex)
    LaunchedEffect(key1 = clipboardFolder) {
        if (!clipboardFolder.isNullOrEmpty() && folderList.value.isEmpty()) {
            viewModel.processIntent(ClipboardListIntent.LoadInitialFolders(clipboardFolder))
        }
    }

    ClipboardListView(
        uiState = uiState,
        clipboardFolderList = folderList.value,
        onShareClick = { item -> onShareClick(item) },
        onIntent = viewModel::processIntent,
        onEditClick = { item -> onEditClick(item) },
        onAddFolderClick = { onAddFolderClick() },
        onJoinFolderClick = { onJoinFolderClick() }
    )


}


@Composable
fun ClipboardListView(
    uiState: ClipboardState,
    clipboardFolderList: List<ClipboardFolder>,
    onShareClick: (ClipboardFolder) -> Unit,
    onIntent: (ClipboardListIntent) -> Unit,
    onEditClick: (ClipboardItem) -> Unit,
    onAddFolderClick: () -> Unit,
    onJoinFolderClick: () -> Unit
) {
    val context = LocalContext.current
    val (isPanelOpen, setPanelOpen) = remember { mutableStateOf(false) }
    val currentList = clipboardFolderList.getOrNull(uiState.currentIndex)
    SlidePanelScaffold(
        isPanelOpen = isPanelOpen,
        onPanelStateChange = setPanelOpen,
        panelContent = {
            SlidePanel(
                folderList = clipboardFolderList,
                onItemClick = {
                    setPanelOpen(false)
                    onIntent(ClipboardListIntent.IndexUpdate(it))
                },
                onAddFolderClick = onAddFolderClick,
                onJoinFolderClick = onJoinFolderClick
            )
        }
    ) {
        if (currentList?.clipboardDateList.isNullOrEmpty()) {
            EmptyListView(
                isShare = currentList?.isShare ?: true,
                onHamburgerBarClick = { setPanelOpen(true) },
                onShareClick = { onShareClick(currentList ?: ClipboardFolder()) })
        } else {
            ClipboardListContent(
                clipboardDateList = currentList?.clipboardDateList ?: listOf(),
                onShareClick = { onShareClick(currentList ?: ClipboardFolder()) },
                isShare = currentList?.isShare ?: true,
                selectedClipboardItem = uiState.selectedItem,
                onHamburgerBarClick = { setPanelOpen(true) },
                onItemClick = { item ->
                    item.url?.let { openUrl(context, it) }
                },
                onItemLongPress = { onIntent(ClipboardListIntent.ItemLongPressed(it)) }
            )
        }

    }


    CustomBottomSheet(
        isOpen = uiState.isSheetOpen,
        item = uiState.selectedItem,
        onDismiss = { onIntent(ClipboardListIntent.BottomSheetDismissed) },
        onDismissEvent = { onIntent(ClipboardListIntent.ClearSelection) },
        onEdit = onEditClick,
        onCopy = { item -> copyToClipboard(context, item.url ?: item.title) },
        onShare = { item -> shareText(context, item.url ?: item.title) },
        onDelete = { item -> onIntent(ClipboardListIntent.Delete(folderId =currentList?.id ?:"" ,itemId = item.itemId,)) },
        onPin = { item ->
            onIntent(
                ClipboardListIntent.TogglePin(
                    clipboardFolderList[uiState.currentIndex].id,
                    item.itemId,
                    item.isPinned
                )
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun ClipboardViewPreView() {
    ClipboardListContent(listOf(dummyData))
}


@Composable
fun ClipboardListContent(
    clipboardDateList: List<ClipboardItem>,
    selectedClipboardItem: ClipboardItem? = null,
    isShare: Boolean = true,
    clickable: Boolean = true,
    onShareClick: () -> Unit = {},
    onHamburgerBarClick: () -> Unit = {},
    onItemClick: (ClipboardItem) -> Unit = {},
    onItemLongPress: (ClipboardItem) -> Unit = {},
) {
    val todayStartTimestamp = getTodayStartTimestamp()
    Log.e("polaris0428", clipboardDateList.toJson())
    val pinnedItems = clipboardDateList
        .filter { it.isPinned }
        .sortedByDescending { it.timestamp }

    val otherItems = clipboardDateList
        .filter { !it.isPinned }
        .sortedByDescending { it.timestamp }

    val todayItems = otherItems.filter { it.timestamp >= todayStartTimestamp }
    val previousItems = otherItems.filter { it.timestamp < todayStartTimestamp }
    val groupedByMonth = previousItems.groupBy {  stringResource(R.string.month_format,getYearMonth(it.timestamp)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {


        HeaderView(isShare, onHamburgerBarClick, onShareClick)


        if (pinnedItems.isNotEmpty()) {
            SectionHeader("Pinned")
            ClipboardCardList(
                items = pinnedItems,
                selectedItem = selectedClipboardItem,
                clickable = clickable,
                onClick = onItemClick,
                onLongPress = onItemLongPress
            )
        }

        if (todayItems.isNotEmpty()) {
            SectionHeader("Today")
            ClipboardCardList(
                items = todayItems,
                selectedItem = selectedClipboardItem,
                clickable = clickable,
                onClick = onItemClick,
                onLongPress = onItemLongPress
            )
        }

        groupedByMonth.forEach { (month, items) ->
            SectionHeader(month)
            ClipboardCardList(
                items = items,
                selectedItem = selectedClipboardItem,
                clickable = clickable,
                onClick = onItemClick,
                onLongPress = onItemLongPress
            )
        }
    }

}

@Composable
fun HeaderView(
    isShare: Boolean,
    onHamburgerBarClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        // 햄버거 아이콘
        Image(
            painter = painterResource(id = R.drawable.ic_hamburger_bar),
            contentDescription = "메뉴",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(24.dp)
                .offset(y = (-1).dp) // 살짝 위로 올림 (핵심!)
                .clickable { onHamburgerBarClick() }
        )
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically

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
        // 가운데 Header

        // 공유 아이콘
        if (isShare) {
            Image(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = "공유",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(24.dp)
                    .offset(y = (-1).dp) // 위로 약간 올려서 정렬 맞춤
                    .clickable { onShareClick() }
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF333333),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}


@Composable
fun ClipboardCardList(
    items: List<ClipboardItem>,
    selectedItem: ClipboardItem?,
    clickable: Boolean,
    onClick: (ClipboardItem) -> Unit,
    onLongPress: (ClipboardItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        items.forEach { item ->
            ClipboardItemView(
                clipboardItem = item,
                selectedClipboardItem = selectedItem,
                clickable = clickable,
                onClick = { onClick(item) },
                onLongPress = { onLongPress(item) }
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}


@Composable
@Preview(showBackground = true)
fun EmptyListView(
    isShare: Boolean = true,
    onHamburgerBarClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
) {
    Column (  modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)){
        HeaderView(isShare, onHamburgerBarClick, onShareClick)
    }
    CDSColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Header()
        Text("추가된 클립이 없어요")
    }
}


@Preview(showBackground = true)
@Composable
fun preView() {
    ClipboardItemView(
        clipboardItem = dummyData,
        selectedClipboardItem = dummyData,
        onLongPress = {})
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



