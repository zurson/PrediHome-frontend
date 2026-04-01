package com.betoniarze.predihome.presentation.theme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

@Composable
fun AutoSizeText(
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier,
    maxFontSize: TextUnit = 50.sp,
    minFontSize: TextUnit = 10.sp,
    maxLines: Int = 1  ,
    color: Color = Color.Black
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val containerWidth = maxWidth.value
        val containerHeight = maxHeight.value

        val fontSize = remember(text, maxLines) {
            calculateAutoFontSizeToFillContainer(
                text = text,
                containerWidth = containerWidth,
                containerHeight = containerHeight,
                maxFontSize = maxFontSize.value,
                minFontSize = minFontSize.value,
                maxLines = maxLines
            )
        }

        Text(
            text = text,
            fontWeight = fontWeight,
            color = color,
            fontSize = fontSize.sp,
            maxLines = maxLines,
            style = TextStyle(),
        )
    }
}

fun calculateAutoFontSizeToFillContainer(
    text: String,
    containerWidth: Float,
    containerHeight: Float,
    maxFontSize: Float,
    minFontSize: Float,
    maxLines: Int
): Float {
    var fontSize = minFontSize
    val step = 1f

    while (fontSize < maxFontSize) {
        val lineHeight = fontSize * 1.2f
        val totalHeight = lineHeight * maxLines

        val estimatedTextWidth = text.length * fontSize * 0.5f

        if (estimatedTextWidth > containerWidth || totalHeight > containerHeight) {
            fontSize -= step
            break
        }

        fontSize += step
    }

    return max(minFontSize, min(fontSize, maxFontSize))
}


