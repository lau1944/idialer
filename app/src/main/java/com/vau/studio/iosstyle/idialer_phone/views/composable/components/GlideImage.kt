package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun GlideImage(
    image: Any,
    scale: ContentScale? = ContentScale.Crop,
    placeholder: Any? = null,
    modifier: Modifier = Modifier.size(25.dp),
) {
    GlideImage(
        imageModel = image,
        contentScale = ContentScale.Crop,
        modifier = modifier,
        placeHolder = placeholder,
        error = placeholder
    )
}