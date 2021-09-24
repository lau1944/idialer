package com.vau.studio.iosstyle.idialer_phone.views.composable.recent_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue

@Composable
fun RecentAppBar(
    onEditMode: Boolean = false,
    onSelected: ((Int) -> Unit)? = null,
    onEdit: ((Boolean) -> Unit)? = null,
    onClear: (() -> Unit)? = null
) {
    val selectionTextStyle = remember { TextStyle(Color.Black, fontSize = 14.sp) }

    TopAppBar(
        backgroundColor = appColor().background,
        elevation = 0.dp
    ) {
        val selectionIndex = remember { mutableStateOf(0) }

        Box(
            modifier = Modifier
                .height(56.dp)
                .padding(8.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (onEditMode) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                onClear?.invoke()
                            },
                    ) {
                        Text(
                            "Clear",
                            style = TextStyle(color = iosBlue, fontSize = 15.sp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Box(
                    modifier = Modifier
                        .clickable {
                            onEdit?.invoke(!onEditMode)
                        },
                ) {
                    Text(
                        if (onEditMode) "Done" else "Edit",
                        style = TextStyle(color = iosBlue, fontSize = 15.sp)
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clip(RoundedCornerShape(8.dp))
            ) {
                val selectionModifier = Modifier
                    .width(80.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(8.dp))

                Row(
                    Modifier
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = (if (selectionIndex.value == 0)
                            selectionModifier.background(color = Color.White)
                        else selectionModifier).clickable {
                            onSelected?.invoke(0)
                            selectionIndex.value = 0
                        },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "All",
                            style = selectionTextStyle,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(
                        modifier = (if (selectionIndex.value == 1)
                            selectionModifier.background(color = Color.White)
                        else selectionModifier).clickable {
                            onSelected?.invoke(1)
                            selectionIndex.value = 1

                        },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Missed",
                            style = selectionTextStyle,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}