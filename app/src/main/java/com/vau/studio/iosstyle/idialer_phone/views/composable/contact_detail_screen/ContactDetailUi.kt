package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vau.studio.iosstyle.idialer_phone.R
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.ContactPageType
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.AssetImage
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosGray
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactDetailViewModel
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.MainViewModel

const val CONTACT_DETAIL_ROUTE = "contact_detail"
const val QUERY_PARAM_FIX = "?number={number}&prevName={prevName}&id={id}"

@Composable
fun ContactDetailUi(
    preName: String? = "",
    number: String,
    id: String? = "",
    mainViewModel: MainViewModel,
    contactViewModel: ContactDetailViewModel,
) {
    Scaffold(
        Modifier.background(iosGray.copy(alpha = 0.1f)),
        topBar = {
            DetailAppbar(prevName = preName!!) {
                mainViewModel.popBack()
            }
        }
    ) {
        val contactDetailState by contactViewModel.contactDetail.observeAsState()

        LaunchedEffect(true) {
            contactViewModel.initState()

            val page = ContactPageType.map(preName)
            if (page == ContactPageType.Recent) {
                contactViewModel.getCallLogByNumber(number)
            }
            if (page == ContactPageType.Contact) {
                contactViewModel.getContactDetailById(id!!)
            }
        }

        UiProgressLayout(state = contactDetailState) {
            val contact: Contact = (contactDetailState as UiState.Success).data as Contact

            LazyColumn(content = {
                item {
                    UserInfoView(contact = contact)

                }
            })
        }
    }
}

/**
 * User info header view
 */
@Composable
private fun UserInfoView(contact: Contact) {
    Column(
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AssetImage(res = R.drawable.ic_big_user, size = 65)
        Text(
            contact.name ?: contact.number?.toString() ?: "Unknown",
            style = TextStyle(color = appColor().surface, fontSize = 24.sp),
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

/**
 * Action row view
 */
@Composable
private fun UserActionView(contact: Contact) {
    Row(

    ) {

    }
}