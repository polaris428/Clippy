package com.polaris.designsystem.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.polaris.designsystem.R
import com.polaris.model.model.ClipboardItem
import com.polaris.util.convertTimestampToMonthDay


@Preview(showBackground = true)
@Composable
fun LineView() {
    Column {
        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(1f), // 1f 제거 (불필요)
            color = Gray90,
            thickness = 1.dp
        )
    }


}

@Preview(showBackground = true)
@Composable
fun CDSButton(
    buttonText: String = "버튼 텍스트",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = PrimaryColor,  // 버튼 배경색
        contentColor = Color.Black   // 버튼 내 텍스트 색상
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit = {}
) {
    ClippyTheme {
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(0.dp),
            enabled = enabled,
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border,
            contentPadding = contentPadding,
            interactionSource = interactionSource
        ) {
            Text(
                text = buttonText,
                modifier = Modifier.padding(6.dp),
                style = MaterialTheme.typography.labelLarge
            )
            content()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CDSNegativeButton(
    buttonText: String = "버튼 텍스트",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = PrimaryGray,  // 버튼 배경색
        contentColor = Color.Black   // 버튼 내 텍스트 색상
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit = {}
) {

    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(1f),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        Text(
            text = buttonText,
            modifier = Modifier.padding(6.dp),
            style = MaterialTheme.typography.labelLarge
        )
        content()
    }
}


@Preview(showBackground = true)
@Composable
fun CDSTransparentButton(
    buttonText: String = "버튼 텍스트",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,  // 버튼 배경색
        contentColor = Gray40   // 버튼 내 텍스트 색상
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit = {}
) {

    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(1f),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        Text(
            text = buttonText,
            modifier = Modifier.padding(6.dp),
            style = MaterialTheme.typography.labelLarge
        )
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun CDSTextField(
    modifier: Modifier = Modifier,
    value: String = "",  // 외부에서 값을 받도록 수정
    onValueChange: (String) -> Unit = {}, // 값이 변경될 때 외부로 전달
    label: String = "라벨",
    textColor: Color = Color.Black,
    underlineColor: Color = UnderlineColor,
    focusedUnderlineColor: Color = Color.Blue,
    underlineThickness: Dp = 2.dp,
    keyboardType: KeyboardType = KeyboardType.Text,
    isFocused: Boolean = false,
    onFocusChange: (Boolean) -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange, // 값이 바뀌면 외부로 전달
            label = { Text(label, color = textColor) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            textStyle = TextStyle(fontSize = 18.sp, color = textColor),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChange(it.isFocused) }
                .offset(x = (-10).dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(underlineThickness)
                .align(Alignment.BottomStart)
                .padding(start = 7.dp)
        ) {
            drawLine(
                color = if (isFocused) focusedUnderlineColor else underlineColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = underlineThickness.toPx()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AnimatedCheckmarkWithCirclePreview() {
    var isVisible by remember { mutableStateOf(true) } // 🔹 디폴트: 안 보이도록 설정

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedCheckmarkWithCircle(isVisible = isVisible)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { isVisible = !isVisible }) {
            Text(if (isVisible) "사라지게 하기" else "보이게 하기")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedCheckmarkWithCircle(isVisible: Boolean = false) {
    var isPreView = LocalInspectionMode.current
    val circleProgress = remember { Animatable(if (isPreView) 1f else 0f) } // 🔹 프리뷰에서 1f 설정
    val checkProgress = remember { Animatable(if (isPreView) 1f else 0f) } // 🔹 프리뷰에서 1f 설정

    LaunchedEffect(isVisible) {
        if (!isPreView) { // 🔹 프리뷰에서는 애니메이션 실행 X (즉시 보이도록)
            if (isVisible) {
                circleProgress.animateTo(1f, animationSpec = tween(150))
                checkProgress.animateTo(1f, animationSpec = tween(150))
            } else {
                checkProgress.animateTo(0f, animationSpec = tween(150)) // 🔹 체크가 먼저 사라짐
                circleProgress.animateTo(0f, animationSpec = tween(150)) // 🔹 원이 나중에 사라짐
            }
        }
    }

    Canvas(modifier = Modifier.size(24.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension / 2.2f
        val currentRadius = maxRadius * circleProgress.value

        drawCircle(
            color = PrimaryColor,
            radius = currentRadius,
            center = center,
            style = Fill
        )

        val checkPath = Path().apply {
            moveTo(size.width * 0.3f, size.height * 0.5f)
            lineTo(size.width * 0.45f, size.height * 0.65f)
            lineTo(size.width * 0.75f, size.height * 0.35f)
        }

        val pathMeasure = PathMeasure()
        pathMeasure.setPath(checkPath, false)
        val length = pathMeasure.length * checkProgress.value

        val drawnPath = Path()
        pathMeasure.getSegment(0f, length, drawnPath, true)

        drawPath(
            path = drawnPath,
            color = Color.White,
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun CDSColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(20.dp),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {

        content()

    }
}
var dummyDateList = listOf(
    ClipboardItem(
        type = "구글",
        url = "https://www.google.com/search?q=%EB%84%A4%EC%9D%B4%EB%B2%84&oq=&gs_lcrp=...",
        title = "Google",
        faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain_url=https://www.google.com/search?q=%EB%84%A4%EC%9D%B4%EB%B2%84&oq=&gs_lcrp=...",
        timestamp = 1740930924019,
        isPinned = false
    ),
    ClipboardItem(
        type = "네이버 포스트",
        url = "https://m.post.naver.com/viewer/postView.naver?volumeNo=38357237&memberNo=57847637",
        title = "[24년도] 콘서트 망원경 이 글 하나로 종결 : 네이버 포스트",
        faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain_url=https://m.post.naver.com/viewer/postView.naver?volumeNo=38357237&memberNo=57847637",
        timestamp = 1741153750278,
        isPinned = false
    ),
    ClipboardItem(
        type = "ㅇㅅㅇ",
        url = "https://m.search.naver.com/search.naver?query=88%ED%8F%AC%EC%B0%A8+%EB%A9%94%EB%89%B4",
        title = "88포차 메뉴 : 네이버 검색",
        faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain_url=https://m.search.naver.com/search.naver?query=88%ED%8F%AC%EC%B0%A8+%EB%A9%94%EB%89%B4",
        timestamp = 1741157005289,
        isPinned = false
    ),
    ClipboardItem(
        type = "Google",
        url = "https://www.google.com/search?client=ms-android-samsung-ss&q=lindor+%EC%B4%88%EC%BD%9C%EB%A6%BF+%EC%95%8C%EC%BD%9C",
        title = "lindor 초콜릿 알콜 - Google 검색",
        faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain_url=https://www.google.com/search?client=ms-android-samsung-ss&q=lindor+%EC%B4%88%EC%BD%9C%EB%A6%BF+%EC%95%8C%EC%BD%9C",
        timestamp = 1741264638943,
        isPinned = false
    )
)
val dummyData = ClipboardItem(
    type = "GitHub",
    url = "https://www.github.com",
    title = "안드로이드 라이브러리 모음",
    faviconUrl = "https://github.githubassets.com/favicon.ico",
    timestamp = System.currentTimeMillis()
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview()
fun ClipboardItemView(clipboardItem :ClipboardItem =dummyData, selectedClipboardItem: ClipboardItem?=null, onClick: () -> Unit={}, onLongPress: () -> Unit={}) {

    Box {
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { onClick() },
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
        AnimatedCheckmarkWithCircle(selectedClipboardItem?.timestamp == clipboardItem.timestamp)
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
