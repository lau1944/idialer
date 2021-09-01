package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AssetImage(@DrawableRes res: Int, size: Int, color: Color?) {
    Image(
        painter = painterResource(id = res),
        modifier = Modifier.size(size.dp),
        contentDescription = "asset_image",
        colorFilter = if (color != null) ColorFilter.tint(color) else null
    )
}