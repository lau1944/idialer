package com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vau.studio.iosstyle.idialer_phone.data.models.KeypadNumber
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.CallButton
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.NumberCircleButton

@Composable
fun KeypadLayout(
    collection: List<List<KeypadNumber>>,
    hasNumber: Boolean? = false,
    onClear: () -> Unit,
    onTap: (String) -> Unit
) {
    val padding = 10.dp
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (subCollection in collection) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
                for (keypad in subCollection) {
                    NumberCircleButton(keypadNumber = keypad, onTap = onTap)
                }
            }
        }

        Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
            CallButton {

            }
            if (hasNumber!!) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "cancel_button",
                    modifier = Modifier
                        .padding(start = 175.dp)
                        .size(35.dp)
                        .clickable(
                            onClick = onClear
                        )
                )
            }
        }
    }
}