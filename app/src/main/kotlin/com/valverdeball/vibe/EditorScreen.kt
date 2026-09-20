package com.valverdeball.vibe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun EditorScreen() {
    val textMeasurer = rememberTextMeasurer()
    val textBuffer = remember {
        TextBuffer("fn main() {\n    println!(\"hello VIBE\");\n}")
    }
    var lines by remember { mutableStateOf(textBuffer.getAllLines()) }

    var cursorVisible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            cursorVisible = !cursorVisible
        }
    }

    val fontSize = 16.sp
    val lineHeightPx = textMeasurer.measure(
        text = "M",
        style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = fontSize, color = Color.White)
    ).size.height.toFloat()
    val leftPadding = 16f

    var cursorLine by remember { mutableIntStateOf(1) }
    var cursorCol by remember { mutableIntStateOf(4) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val charWidth = textMeasurer.measure(
        text = "m",
        style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = fontSize)
    ).size.width.toFloat()

    Box(modifier = Modifier.fillMaxSize()) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = { new ->
                val typed = new.text.getOrNull(new.text.length - 1)
                if (typed != null) {
                    val (newLine, newCol) = textBuffer.insertChar(cursorLine, cursorCol, typed)
                    cursorLine = newLine
                    cursorCol = newCol
                    lines = textBuffer.getAllLines()
                }
                textFieldValue = TextFieldValue("")
            },
            modifier = Modifier
            .focusRequester(focusRequester)
            .size(1.dp)
        )
    }

    Canvas(
        modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { tapOffset ->
                val tappedLine = (tapOffset.y / lineHeightPx)
                .toInt()
                .coerceIn(0, lines.lastIndex)
                val lineLength = lines[tappedLine].length
                val tappedCol = ((tapOffset.x - leftPadding) / charWidth)
                .roundToInt()
                .coerceIn(0, lineLength)

                cursorLine = tappedLine
                cursorCol = tappedCol
                cursorVisible = true
                focusRequester.requestFocus()
            }
        }
    ) {
        lines.forEachIndexed { index, line ->
            val result = textMeasurer.measure(
                text = line,
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = fontSize,
                    color = Color.White
                )
            )
            drawText(
                textLayoutResult = result,
                topLeft = Offset(x = leftPadding, y = index * lineHeightPx)
            )
        }

        if (cursorVisible) {
            val cursorX = leftPadding + (cursorCol * charWidth)

            val cursorLineLayout = textMeasurer.measure(
                text = lines[cursorLine].ifEmpty { " " },
                style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = fontSize, color = Color.White)
            )
            val cursorTop = cursorLine * lineHeightPx
            val cursorBottom = cursorTop + cursorLineLayout.size.height

            drawLine(
                color = Color.White,
                start = Offset(cursorX, cursorTop),
                end = Offset(cursorX, cursorBottom),
                strokeWidth = 3f
            )
        }
    }
}
