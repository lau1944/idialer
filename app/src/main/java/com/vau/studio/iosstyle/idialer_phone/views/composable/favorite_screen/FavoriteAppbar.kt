package com.vau.studio.iosstyle.idialer_phone.views.composable.favorite_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.FavoriteViewModel

@Composable
fun FavoriteAppbar(
    favoriteViewModel: FavoriteViewModel
) {
    TopAppBar(
        backgroundColor = appColor().background,
        elevation = 0.dp,
        contentPadding = PaddingValues(13.dp),
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            AssetImage(res = R.drawable.ic_add, size = 25)
            Text(
                text = "Favorites",
                style = TextStyle(color = appColor().surface, fontSize = 16.sp)
            )
            Text(
                text = "Edit",
                style = TextStyle(color = iosBlue, fontSize = 16.sp)
            )
        }
    }
}