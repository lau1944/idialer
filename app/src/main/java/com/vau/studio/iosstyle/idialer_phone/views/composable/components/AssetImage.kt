package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AssetImage(
    modifier: Modifier = Modifier,
    @DrawableRes res: Int,
    size: Int,
    isCircleShape: Boolean? = false,
    color: Color? = null,
) {
    Image(
        painter = painterResource(id = res),
        contentScale= ContentScale.Crop,
        modifier = modifier.size(size.dp).apply {
            if (isCircleShape!!) {
                clip(CircleShape)
            }
        },
        contentDescription = "asset_image",
        colorFilter = if (color != null) ColorFilter.tint(color) else null
    )
}