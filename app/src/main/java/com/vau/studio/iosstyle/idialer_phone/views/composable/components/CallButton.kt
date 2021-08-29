package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.core.ColorUtils

@Composable
fun CallButton(onPressed: () -> Unit) {
    CircleButton(
        content = {
            Image(
                painterResource(id = R.drawable.ic_phone),
                modifier = Modifier.size(25.dp),
                contentDescription = "phone",
                colorFilter = tint(Color.White)
            )
        }, onTap = onPressed, color = ColorUtils.parseColor("#19e660")
    )
}