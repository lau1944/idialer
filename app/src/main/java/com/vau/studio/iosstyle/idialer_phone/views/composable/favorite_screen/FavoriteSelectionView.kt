package com.vau.studio.iosstyle.idialer_phone.views.composable.favorite_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.vau.studio.iosstyle.idialer_phone.data.CONTACT_READ_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.CONTACT_WRITE_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.SearchBar
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.contact_screen.ContactItem
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosBlue
import com.vau.studio.iosstyle.idialer_phone.views.composable.iosGray
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactViewModel

@ExperimentalPermissionsApi
@Composable
fun FavoriteSelectionView(
    contactViewModel: ContactViewModel,
    onSelected: (Contact) -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxSize()
            .background(appColor().background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose a contact to add a Favorites",
                style = TextStyle(fontSize = 12.sp, color = appColor().surface),
                modifier = Modifier.padding(5.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .weight(1f)
                    .background(iosBlue))
                Text(
                    "Contacts",
                    style = TextStyle(
                        color = appColor().surface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "Cancel",
                    style = TextStyle(
                        color = iosBlue,
                        fontSize = 15.sp
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End)
                        .clickable {
                            onCancel()
                        }
                )
            }
            Box(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
            ) {

            }
            ContactsSelectionList(contactViewModel)
        }
    }
}

@SuppressLint("PermissionLaunchedDuringComposition")
@ExperimentalPermissionsApi
@Composable
private fun ContactsSelectionList(contactViewModel: ContactViewModel) {
    val contactState by contactViewModel.contactListState.observeAsState()
    val contactPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            CONTACT_READ_PERMISSION,
            CONTACT_WRITE_PERMISSION
        )
    )

    if (contactPermissionsState.allPermissionsGranted) {
        LaunchedEffect(true, block = {
            contactViewModel.getContactNames()
        })

        UiProgressLayout(state = contactState) {
            val contacts = (contactState as UiState.Success).data as List<Contact>
            LazyColumn(
                content = {
                    items(contacts,
                        itemContent = { contact ->
                            ContactItem(contact.name)
                        })
                })
        }
    } else {
        contactPermissionsState.launchMultiplePermissionRequest()
    }
}