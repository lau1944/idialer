package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor

@Composable
fun InfoViewHolder(modifier: Modifier? = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier!!
            .clip(RoundedCornerShape(16.dp))
            .shadow(5.dp)
            .background(appColor().background)
            .padding(horizontal = 10.dp)
    ) {
        content()
    }
}