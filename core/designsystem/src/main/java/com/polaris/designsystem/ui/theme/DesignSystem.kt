package com.polaris.designsystem.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Divider
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview(showBackground = true)
@Composable
fun LineView() {
    Column {
        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(), // 1f 제거 (불필요)
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