package com.vau.studio.iosstyle.idialer_phone.views.composable.setting_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.BuildConfig
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.core.OpenUtil
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.InfoViewHolder
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlack
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosGray
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosWhite

@Composable
fun SettingView() {
    val context = LocalContext.current

    Scaffold(
        backgroundColor = iosGray.copy(0.4f),
        topBar = {
            SettingAppbar()
        }
    ) {
        LazyColumn(content = {
            item {
                RateAppView(onRate = {
                    OpenUtil.openMyApp(context, BuildConfig.APPLICATION_ID)
                })
                OpenAppStore {
                    OpenUtil.openMyApp(context, "com.vau.apphunt.studiotech")
                }
            }
        })
    }
}

@Composable
private fun SettingAppbar() {
    TopAppBar(
        backgroundColor = iosWhite,
    ) {
        Text("Setting", modifier = Modifier.padding(horizontal = 15.dp))
    }
}

@Composable
private fun OpenAppStore(onOpen : () -> Unit) {
    InfoViewHolder(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .clickable {
                onOpen()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 5.dp)
        ) {
            AssetImage(res = R.drawable.apphunt_icon, size = 25)
            Text(
                "Open Our App Store",
                style = TextStyle(color = iosBlack, fontSize = 16.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
private fun RateAppView(onRate: () -> Unit) {
    InfoViewHolder(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .clickable {
                onRate()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 5.dp)
        ) {
            AssetImage(res = R.drawable.ic_rate_star, size = 25)
            Text(
                "Rate me",
                style = TextStyle(color = iosBlack, fontSize = 16.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}