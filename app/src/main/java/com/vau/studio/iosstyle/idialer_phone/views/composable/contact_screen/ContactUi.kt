package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_screen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.vau.studio.iosstyle.idialer_phone.core.OpenUtil
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.ContactViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactUi(
    multiplePermissionsState: MultiplePermissionsState,
    contactViewModel: ContactViewModel
) {
    val scrollState = rememberLazyListState()

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
                multiplePermissionsState.allPermissionsGranted -> {
                    Box(
                        modifier = Modifier.padding(horizontal = 5.dp),
                    ) {
                        val contactState by contactViewModel.contactListState.observeAsState()

                        LaunchedEffect(true, block = {
                            contactViewModel.getContactNames()
                        })

                        UiProgressLayout(state = contactState) {
                            val contacts = (contactState as UiState.Success).data as List<String>
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
                multiplePermissionsState.shouldShowRationale ||
                        !multiplePermissionsState.allPermissionsGranted -> {
                    Button(onClick = {
                        multiplePermissionsState.launchMultiplePermissionRequest()
                    }) {
                        Text(
                            "Grant Contact Permission", style = TextStyle(
                                appColor().background,
                                fontSize = 16.sp,
                            )
                        )
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

@Composable
private fun ContactList(contactNames: List<String>, scrollState: LazyListState) {
    LazyColumn(content = {
        items(contactNames) { name ->
            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 10.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        name,
                        style = TextStyle(color = appColor().surface, fontSize = 16.sp),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                }
            }
        }
    },
    verticalArrangement = Arrangement.Top,
    state = scrollState)
}

@Composable
private fun DeniedLayout(navigateToSetting: () -> Unit) {
    Column {
        Text("Permission denied, open setting to grant permission")
        Button(onClick = {
            navigateToSetting()
        }) {
            Text(
                "Open Setting", style = TextStyle(
                    appColor().background,
                    fontSize = 16.sp,
                )
            )
        }
    }
}