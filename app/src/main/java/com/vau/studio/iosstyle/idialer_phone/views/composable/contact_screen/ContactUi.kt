package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.vau.studio.iosstyle.idialer_phone.core.OpenUtil
import com.vau.studio.iosstyle.idialer_phone.data.CONTACT_READ_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.CONTACT_WRITE_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.DeniedLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactViewModel
import org.intellij.lang.annotations.JdkConstants

@ExperimentalFoundationApi
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactUi(
    contactViewModel: ContactViewModel
) {
    val scrollState = rememberLazyListState()
    val contactPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            CONTACT_READ_PERMISSION,
            CONTACT_WRITE_PERMISSION
        )
    )

    Scaffold(
        topBar = {
            ContactAppBar(scrollState, contactViewModel)
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            val context = LocalContext.current
            when {
                // All permission granted here
                contactPermissionsState.allPermissionsGranted -> {
                    Box(
                        modifier = Modifier.padding(horizontal = 5.dp),
                    ) {
                        val contactState by contactViewModel.contactListState.observeAsState()

                        LaunchedEffect(true, block = {
                            contactViewModel.getContactNames()
                        })

                        UiProgressLayout(state = contactState) {
                            val contacts = (contactState as UiState.Success).data as List<Contact>
                            if (contacts.isNullOrEmpty()) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text("No Contact Info", style = TextStyle(color = Color.Black))
                                }
                            } else {
                                ContactList(contactNames = contacts, scrollState = scrollState)
                            }
                        }
                    }
                }

                // Permission did not request
                contactPermissionsState.shouldShowRationale ||
                        !contactPermissionsState.allPermissionsGranted -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button(onClick = {
                            contactPermissionsState.launchMultiplePermissionRequest()
                        }) {
                            Text(
                                "Grant Contact Permission", style = TextStyle(
                                    appColor().background,
                                    fontSize = 16.sp,
                                )
                            )
                        }
                    }
                }

                // permission denied
                else -> {
                    DeniedLayout {
                        OpenUtil.openSetting(context)
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun ContactList(contactNames: List<Contact>, scrollState: LazyListState) {
    val groupedContacts = contactNames.groupBy { it.name?.first().toString() }

    LazyColumn(
        content = {
            groupedContacts.forEach { (initial, contactsInitial) ->
                stickyHeader {
                    CharacterHeader(character = initial)
                }

                items(contactsInitial) { contact ->
                    ContactItem(contact.name)
                }
            }
        },
        verticalArrangement = Arrangement.Top,
        state = scrollState,
    )
}

@Composable
private fun CharacterHeader(character: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        Text(
            character,
            style = TextStyle(
                color = appColor().surface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(top = 8.dp)
                .background(color = Color.LightGray.copy(alpha = 0.4f))
        )
    }
}