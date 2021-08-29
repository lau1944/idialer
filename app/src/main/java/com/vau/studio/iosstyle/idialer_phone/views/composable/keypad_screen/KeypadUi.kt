package com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vau.studio.iosstyle.idialer_phone.data.models.KeypadNumber
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.NumberCircleButton

@Composable
fun DialerScreen(onTap: (String) -> Unit) {
    val padding = 10.dp
    val collection = remember { getKeypadCollections() }

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
                    NumberCircleButton(keypadNumber = keypad, onTap = { num ->

                    })
                }
            }
        }
    }
}

private fun getKeypadCollections(): List<List<KeypadNumber>> {
    val keypadList = arrayListOf<List<KeypadNumber>>()
    keypadList.add(
        listOf(
            KeypadNumber("1"),
            KeypadNumber("2", listOf("A", "B", "C")),
            KeypadNumber("3", listOf("D", "E", "F"))
        )
    )
    keypadList.add(
        listOf(
            KeypadNumber("4", listOf("G", "H", "I")),
            KeypadNumber("5", listOf("J", "K", "L")),
            KeypadNumber("6", listOf("M", "N", "O"))
        )
    )
    keypadList.add(
        listOf(
            KeypadNumber("7", listOf("P", "Q", "R", "S")),
            KeypadNumber("8", listOf("T", "U", "V")),
            KeypadNumber("9", listOf("W", "X", "Y", "Z"))
        )
    )
    keypadList.add(
        listOf(
            KeypadNumber("*"),
            KeypadNumber("0", listOf("+")),
            KeypadNumber("#")
        )
    )
    return keypadList
}