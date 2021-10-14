package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.vau.studio.iosstyle.idialer_phone.core.DeviceUtil

@Composable
fun noElevation() : ButtonElevation{
    return ButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        disabledElevation = 0.dp
    )
}

@Composable
fun keyButtonSize() : Int {
    return when (DeviceUtil.info?.width) {
        in 0..599 -> 90
        in 600..904 -> 120
        else -> 140
    }
}