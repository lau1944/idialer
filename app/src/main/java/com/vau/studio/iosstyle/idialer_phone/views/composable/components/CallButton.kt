package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.core.ColorUtils

@Composable
fun CallButton(onPressed: () -> Unit) {
    CircleButton(
        content = {
            AssetImage(res = R.drawable.ic_phone, size = 25, color = Color.White)
        }, onTap = onPressed, color = ColorUtils.parseColor("#19e660")
    )
}