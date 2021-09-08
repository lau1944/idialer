package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor

@Composable
fun DeniedLayout(navigateToSetting: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Permission denied, open setting to grant permission")
        Button(onClick = {
            navigateToSetting()
        }) {
            Text(
                "Open Setting", style = TextStyle(
                    appColor().background,
                    fontSize = 16.sp,
                )
            )
        }
    }
}