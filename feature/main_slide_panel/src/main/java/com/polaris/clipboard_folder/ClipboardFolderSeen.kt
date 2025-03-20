package com.polaris.clipboard_folder

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.polaris.designsystem.R
import com.polaris.designsystem.ui.theme.CDSColumn
import com.polaris.designsystem.ui.theme.LineView
import com.polaris.model.model.ClipboardFolder
import com.polaris.util.toJson
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


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

@Composable
@Preview(showBackground = true)
fun ClipboardFolderViewPreView() {
    Box {
        Column() {
            Header()
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                folderItem(clipboardFolder = ClipboardFolder(name = "모든 노트"))
                folderItem(clipboardFolder = ClipboardFolder(name = "공유 노트"))
                folderItem(clipboardFolder = ClipboardFolder(name = "개인 노트"))

            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                folderItem(clipboardFolder = ClipboardFolder(name = "고정됨"))
                folderItem(clipboardFolder = ClipboardFolder(name = "최근 삭제됨"))
            }


        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview(showBackground = true)
fun folderItem(
    clipboardFolder: ClipboardFolder = ClipboardFolder(),
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(clipboardFolder.id) },
                onLongClick = {
                    // onLongPress() // ✅ 기존 롱클릭 이벤트 실행
                },
                indication = rememberRipple(
                    color = Color.Gray, // 리플 색상 설정
                    bounded = true // Row 크기 내에서만 리플 퍼지게 설정
                ),
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(top = 14.dp, start = 12.dp)
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_folder),
            contentDescription = ""
        )

        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Text(clipboardFolder.name, style = MaterialTheme.typography.bodyLarge)
            LineView()
        }

        // 오른쪽 끝에 정렬될 요소들
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "0", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = ""
            )

            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
fun SlidePanel(
    folderList: List<ClipboardFolder>,
    onItemClick: (String) -> Unit,
    onAddFolderClick: () -> Unit,
    isPanelOpen: Boolean = false,
    rawDragOffset: Float = 0f,
    isDragging: Boolean = false,

    panelClose:()->Unit ={}
) {


    // ✅ 드래그 중에는 즉시 반영, 드래그 종료 후 애니메이션 적용
    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isDragging) rawDragOffset else rawDragOffset,
        animationSpec = if (isDragging) snap() else tween(250, easing = FastOutSlowInEasing),
        label = "animatedOffsetX"
    )



    Box(
        modifier = Modifier
            .fillMaxHeight()

    ) {

        Column(modifier = Modifier.background(Color.Transparent)) {

            SlidePanelContent(
                folderList = folderList,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            animatedOffsetX.roundToInt(),
                            0
                        )
                    } // ✅ 드래그 중 즉시 반응 + 드래그 종료 후 애니메이션 적용
                    .fillMaxHeight()
                    .fillMaxWidth(0.7f)
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                    )
                    .padding(16.dp),
                onItemClick = onItemClick,
                onAddFolderClick=onAddFolderClick

            )


        }
    }

}


/**
 * ✅ 패널 내부 버튼 UI
 */
@Composable
fun SlidePanelContent(
    folderList: List<ClipboardFolder>,
    modifier: Modifier,
    onItemClick: (String) -> Unit,
    onAddFolderClick:()->Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Header()

        LazyColumn(
            modifier = Modifier // LazyColumn은 가변 크기
                .fillMaxWidth()
        ) {
            items(folderList) { item ->
                folderItem(item, onItemClick)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            folderItem(clipboardFolder = ClipboardFolder(id = "0", name = "고정됨"))
            folderItem(clipboardFolder = ClipboardFolder(id = "-1", name = "최근 삭제됨"))
        }

        // 📌 여기에 Spacer(weight=1f)를 추가하여 아래 여백을 만듦!
        Spacer(modifier = Modifier.weight(1f))

        Text(
            "폴더 추가하기",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAddFolderClick() }
                .padding(16.dp)
                .align(Alignment.CenterHorizontally) // 중앙 정렬
        )
    }
}


/**
 * ✅ 패널 내 버튼 컴포넌트
 */
@Composable
fun PanelButton(clipboardFolder: ClipboardFolder, onClick: (String) -> Unit) {
    TextButton(
        onClick = { onClick(clipboardFolder.id) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(clipboardFolder.name)
    }
}


