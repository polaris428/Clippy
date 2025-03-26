package com.polaris.clipboard_list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
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


@Composable
fun ClipboardListSeen(
    clipboardFolder: List<ClipboardFolder>? = null,
    onEditClick: (item: ClipboardItem) -> Unit,
    onAddFolderClick: () -> Unit,
    onJoinFolderClick: () -> Unit
) {

    val viewModel: ClipboardListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val folderList = viewModel.folderList.collectAsState()
    LaunchedEffect(key1 = clipboardFolder) {
        if (!clipboardFolder.isNullOrEmpty() && folderList.value.isEmpty()) {
            viewModel.processIntent(ClipboardListIntent.LoadInitialFolders(clipboardFolder))
        }
    }
    if (clipboardFolder.isNullOrEmpty()){
        EmptyListView()
    }else{
        ClipboardListView(
            uiState = uiState,
            clipboardFolderList=folderList.value,
            onIntent = viewModel::processIntent,
            onEditClick = { item -> onEditClick(item) },
            onAddFolderClick = { onAddFolderClick() },
            onJoinFolderClick = {onJoinFolderClick()}
        )

    }



}




@Composable
fun ClipboardListView(
    uiState:ClipboardState,
    clipboardFolderList:List<ClipboardFolder>,

    onIntent: (ClipboardListIntent) -> Unit,
    onEditClick: (ClipboardItem) -> Unit,
    onAddFolderClick: () -> Unit,
    onJoinFolderClick: () -> Unit
) {
    val context = LocalContext.current

    SlidePanelScaffold(

        panelContent = {
            SlidePanel(
                folderList = clipboardFolderList,
                onItemClick = { onIntent(ClipboardListIntent.IndexUpdate(it)) },
                onAddFolderClick = onAddFolderClick,
                onJoinFolderClick = onJoinFolderClick
            )
        }
    ) {
        ClipboardListContent(
            clipboardDateList = clipboardFolderList.getOrNull(uiState.currentIndex)?.clipboardDateList ?: listOf(),
            selectedClipboardItem = uiState.selectedItem,
            onClick = { item ->
                item.url?.let { openUrl(context, it) }
            },
            onLongPress = { onIntent(ClipboardListIntent.ItemLongPressed(it)) }
        )
    }


    CustomBottomSheet(
        isOpen = uiState.isSheetOpen,
        item = uiState.selectedItem,
        onDismiss = { onIntent(ClipboardListIntent.BottomSheetDismissed) },
        onDismissEvent = { onIntent(ClipboardListIntent.ClearSelection) },
        onEdit = onEditClick,
        onCopy = { item -> copyToClipboard(context, item.url ?: item.title) },
        onShare = { item -> shareText(context, item.url ?: item.title) },
        onDelete = { item -> onIntent(ClipboardListIntent.Delete(item.timestamp)) },
        onPin = { item -> onIntent(ClipboardListIntent.TogglePin(  clipboardFolderList[uiState.currentIndex].id,item.itemId, item.isPinned)) }
    )
}


@Preview(showBackground = true)
@Composable
fun ClipboardViewPreView(){
    ClipboardListContent(listOf(dummyData))
}


@Composable
fun ClipboardListContent(
    clipboardDateList: List<ClipboardItem>,
    selectedClipboardItem: ClipboardItem? = null,
    clickable: Boolean = true,
    onClick: (ClipboardItem) -> Unit = {},
    onLongPress: (ClipboardItem) -> Unit = {},
) {
    val todayStartTimestamp = getTodayStartTimestamp()
    Log.e("polaris0428",clipboardDateList.toJson())
    val pinnedItems = clipboardDateList
        .filter { it.isPinned }
        .sortedByDescending { it.timestamp }

    val otherItems = clipboardDateList
        .filter { !it.isPinned }
        .sortedByDescending { it.timestamp }

    val todayItems = otherItems.filter { it.timestamp >= todayStartTimestamp }
    val previousItems = otherItems.filter { it.timestamp < todayStartTimestamp }
    val groupedByMonth = previousItems.groupBy { getYearMonth(it.timestamp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Header()

        if (pinnedItems.isNotEmpty()) {
            SectionHeader("Pinned")
            ClipboardCardList(
                items = pinnedItems,
                selectedItem = selectedClipboardItem,
                clickable = clickable,
                onClick = onClick,
                onLongPress = onLongPress
            )
        }

        if (todayItems.isNotEmpty()) {
            SectionHeader("Today")
            ClipboardCardList(
                items = todayItems,
                selectedItem = selectedClipboardItem,
                clickable = clickable,
                onClick = onClick,
                onLongPress = onLongPress
            )
        }

        groupedByMonth.forEach { (month, items) ->
            SectionHeader(month)
            ClipboardCardList(
                items = items,
                selectedItem = selectedClipboardItem,
                clickable = clickable,
                onClick = onClick,
                onLongPress = onLongPress
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
fun EmptyListView() {
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



