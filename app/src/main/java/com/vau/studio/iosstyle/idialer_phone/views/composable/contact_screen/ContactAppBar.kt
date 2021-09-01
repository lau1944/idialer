package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosWhite

@Composable
fun ContactAppBar() {
    TopAppBar(
        backgroundColor = iosWhite,
        elevation = 0.dp,
        contentPadding = PaddingValues(horizontal = 15.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            AssetImage(res = R.drawable.ic_plus, size = 20, color = iosBlue)
        }
    }
}