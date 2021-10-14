package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory

@Composable
fun SwipeableCard(
    modifier: Modifier? = Modifier,
    maxCancelAreaWidth: Float = 95f,
    onDrag: ((Float) -> Unit)? = null,
    endContent: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val cancelAreaWidth = remember { mutableStateOf(0f) }

    Box(
        modifier = modifier!!
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        change.consumeAllChanges()

                        onDrag?.invoke(dragAmount)
                        cancelAreaWidth.value += -dragAmount
                    },
                    onDragEnd = {
                        if (cancelAreaWidth.value < maxCancelAreaWidth / 2) {
                            cancelAreaWidth.value = 0f
                        } else {
                            cancelAreaWidth.value = maxCancelAreaWidth
                        }
                    },
                )
            }
    ) {
        Row() {
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
            if (endContent != null)
                EndArea(width = cancelAreaWidth.value) {
                    endContent()
                }
        }
    }
}

@Composable
private fun EndArea(width: Float, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .width(width.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}