// lazy to write commentaries, sorry, atleast you have... 'javadoc' in TextBuffer
package com.valverdeball.vibe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import kotlinx.coroutines.delay

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

    var textFieldValue by remember {
        val initialText = textBuffer.fullText()
        mutableStateOf(TextFieldValue(text = initialText, selection = TextRange(initialText.length)))
    }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val charWidth = textMeasurer.measure(
        text = "m",
        style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = fontSize)
    ).size.width.toFloat()

    fun offsetToLineCol(text: String, offset: Int): Pair<Int, Int> {
        var line = 0
        var lastNewline = -1
        for (i in 0 until offset) {
            if (text[i] == '\n') {
                line++
                lastNewline = i
            }
        }
        val col = offset - lastNewline - 1
        return Pair(line, col)
    }

    val (cursorLine, cursorCol) = offsetToLineCol(textFieldValue.text, textFieldValue.selection.start)

    Box(modifier = Modifier.fillMaxSize()) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = { new ->
                textBuffer.setFullText(new.text)
                lines = textBuffer.getAllLines()
                textFieldValue = new
                cursorVisible = true
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
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

                var flatOffset = 0
                for (i in 0 until tappedLine) {
                    flatOffset += lines[i].length + 1
                }
                flatOffset += tappedCol

                textFieldValue = textFieldValue.copy(selection = TextRange(flatOffset))
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
                text = lines.getOrElse(cursorLine) { " " }.ifEmpty { " " },
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
