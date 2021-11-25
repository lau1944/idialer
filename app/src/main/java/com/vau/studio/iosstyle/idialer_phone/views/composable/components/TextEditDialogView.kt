package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.views.composable.*

@ExperimentalComposeUiApi
@Composable
fun TextEditDialogView(
    title: String,
    hint: String = "",
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val context = LocalContext.current
    val inputText = remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(iosGray.copy(0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(appColor().background)
                .padding(25.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    title,
                    style = TextStyle(fontSize = 13.sp),
                    modifier = Modifier.padding(vertical = 15.dp)
                )
                StandardEditText(
                    value = inputText.value,
                    hint = hint,
                    onChange = {
                        inputText.value = it
                    },
                    onDone = {
                        onConfirm.invoke(inputText.value)
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DisposeButton {
                        onCancel()
                    }
                    ConfirmButton {
                        val input = inputText.value
                        if (input.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Input text should not be empty.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onConfirm.invoke(inputText.value)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfirmButton(onConfirm: () -> Unit) {
    Button(
        onClick = onConfirm,
        colors = ButtonDefaults.buttonColors(contentColor = iosBlue),
        shape = RoundedCornerShape(20.dp),
    ) {
        Text(
            "Confirm",
            style = TextStyle(fontSize = 16.sp, color = iosWhite),
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

@Composable
private fun DisposeButton(onCancel: () -> Unit) {
    OutlinedButton(
        onClick = onCancel,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, iosRed),
        modifier = Modifier.padding(vertical = 10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = iosRed,
            backgroundColor = Color.Transparent
        )
    ) {
        Text(
            "Cancel",
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}