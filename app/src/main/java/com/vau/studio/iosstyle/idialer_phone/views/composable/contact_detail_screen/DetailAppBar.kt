package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_detail_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue

@Composable
fun DetailAppbar(prevName: String, backgroundColor: Color, onEdit: () -> Unit, onBack: () -> Unit) {
    TopAppBar(
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        contentPadding = PaddingValues(7.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable {
                            onBack()
                        }
                ) {
                    AssetImage(res = R.drawable.ic_back, size = 20, color = iosBlue)
                    Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(prevName, style = TextStyle(color = iosBlue, fontSize = 16.sp))
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable {
                            onEdit()
                        }
                ) {
                    Text("Edit", style = TextStyle(color = iosBlue, fontSize = 16.sp))
                }
            }
        }
    }
}