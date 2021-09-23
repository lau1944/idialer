package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue

@Composable
fun SelectionDialog(
    options: List<SelectionOption>,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (option in options) {
                    SelectionButton(
                        text = option.text,
                        textColor = Color.Red,
                        backgroundColor = appColor().background.copy(alpha = 0.9f)
                    ) {
                        option.onTap?.invoke()
                    }
                }

                SelectionButton(text = "Cancel") {
                    onDismiss()
                }
            }
        }
    }
}

@Composable
private fun SelectionButton(
    text: String,
    textColor: Color? = iosBlue,
    backgroundColor: Color? = appColor().background,
    onTap: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor!!)
            .fillMaxWidth()
            .clickable { onTap() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(color = textColor!!),
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 14.dp)
        )
    }
}

data class SelectionOption(
    val text: String,
    val onTap: (() -> Unit)? = null
)