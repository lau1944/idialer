package com.vau.studio.iosstyle.idialer_phone.views.composable.favorite_screen

import androidx.compose.foundation.clickable
import com.vau.studio.iosstyle.idialer_phone.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.GlideImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosGray

@Composable
fun FavoriteItem(contact: Contact, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(75.dp)
            .clickable {
                onClick?.invoke()
            }
            .padding(horizontal = 8.dp, vertical = 13.dp)
    ) {
        Row {
            GlideImage(
                imageUrl = contact.phoneUrl ?: "",
                modifier = Modifier.size(35.dp),
                placeholder = ImageBitmap.imageResource(id = R.drawable.ic_user)
            )
            UserContentView(contact = contact)
            AssetImage(res = R.drawable.ic_info, size = 20, color = iosGray.copy(alpha = 0.5f))
        }
    }
}

@Composable
private fun UserContentView(contact: Contact) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = contact.name ?: contact.number ?: "unknown",
            style = TextStyle(
                color = appColor().surface,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
        Row(
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            AssetImage(
                res = R.drawable.ic_message,
                size = 15,
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(
                text = "Messages",
                style = TextStyle(
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(end = 5.dp)
            )
        }
    }
}