package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vau.studio.iosstyle.idialer_phone.data.models.KeypadNumber

@Composable
fun KeypadLayout(collection: List<List<KeypadNumber>>, onTap: (String) -> Unit) {
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

        Box(modifier = Modifier.padding(10.dp)) {
            CallButton {

            }
        }
    }
}