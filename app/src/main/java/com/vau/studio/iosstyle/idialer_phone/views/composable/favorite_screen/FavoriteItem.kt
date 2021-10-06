package com.vau.studio.iosstyle.idialer_phone.views.composable.favorite_screen

import com.vau.studio.iosstyle.idialer_phone.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage

@Composable
fun FavoriteItem(contact: Contact) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(horizontal = 8.dp, vertical = 13.dp)
    ) {
        Row() {
            GlideImage(imageModel = contact.phoneUrl ?: "", contentScale = ContentScale.Crop)
            UserContentView(contact = contact) {

            }
            AssetImage(res = R.drawable.ic_info, size = 20)
        }
    }
}

@Composable
private fun UserContentView(contact: Contact, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = contact.name ?: "",
            style = TextStyle(
                color = appColor().surface,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
        Row {
            AssetImage(res = R.drawable.ic_message, size = 15)
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