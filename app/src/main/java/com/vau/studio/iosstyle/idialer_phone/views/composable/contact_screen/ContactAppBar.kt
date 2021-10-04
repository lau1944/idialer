package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_screen

import android.provider.ContactsContract
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.SearchBar
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosWhite
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactViewModel

@Composable
fun ContactAppBar(
    scrollState: LazyListState,
    contactViewModel: ContactViewModel
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {

        TopAppBar(
            backgroundColor = appColor().background,
            elevation = 0.dp,
            contentPadding = PaddingValues(horizontal = 15.dp),
        ) {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                AssetImage(res = R.drawable.ic_plus, size = 20, color = iosBlue)
            }
        }
        Column {
            Text(
                "Contacts",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            )
            Box(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp)
            ) {
                SearchBar(onChanged = {
                    searchContactByName(it, contactViewModel = contactViewModel)
                }, onSearch = {
                    searchContactByName(it, contactViewModel = contactViewModel)
                })
            }
        }
    }
}

private fun searchContactByName(name: String?, contactViewModel: ContactViewModel) {
    contactViewModel.queryNumberByName(name)
}