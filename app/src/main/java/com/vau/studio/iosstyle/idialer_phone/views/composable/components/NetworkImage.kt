package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NetworkImage(
    modifier: Modifier? = Modifier.size(25.dp),
    imageUrl: String,
    scale: ContentScale? = ContentScale.Crop,
    placeholder: Any? = null
) {
    GlideImage(
        imageModel = imageUrl,
        contentScale = ContentScale.Crop,
        modifier = modifier!!,
        placeHolder = placeholder,
        error = placeholder
    )
}