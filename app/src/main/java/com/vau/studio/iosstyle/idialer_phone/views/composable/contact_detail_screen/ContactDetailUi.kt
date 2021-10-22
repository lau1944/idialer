package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosGray
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.MainViewModel

const val CONTACT_DETAIL_ROUTE = "contact_detail"
const val QUERY_PARAM_FIX = "?number={number}&prevName={prevName}"

@Composable
fun ContactDetailUi(
    preName: String? = "",
    number: String?,
    mainViewModel: MainViewModel,
) {
    Scaffold(
        Modifier.background(iosGray.copy(alpha = 0.1f)),
        topBar = {
            DetailAppbar(prevName = preName!!) {
                mainViewModel.popBack()
            }
        }
    ) {
        LazyColumn(content = {
            item {


            }
        })
    }
}

@Composable
private fun UserInfoView(contact: Contact) {
    Column(
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AssetImage(res = R.drawable.ic_big_user, size = 45)
        Text(
            contact.name ?: contact.number ?: "Unknown",
            style = TextStyle(color = appColor().surface, fontSize = 24.sp),
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}