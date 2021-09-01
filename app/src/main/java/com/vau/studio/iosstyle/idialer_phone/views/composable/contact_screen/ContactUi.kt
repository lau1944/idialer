package com.vau.studio.iosstyle.idialer_phone.views.composable.contact_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.material.button.MaterialButton
import com.vau.studio.iosstyle.idialer_phone.core.OpenUtil
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.SearchBar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactUi(
    multiplePermissionsState: MultiplePermissionsState
) {
    Scaffold(
        topBar = {
            ContactAppBar()
        },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current
            when {
                // All permission granted here
                multiplePermissionsState.allPermissionsGranted -> {
                    Box(
                        modifier = Modifier.padding(horizontal = 5.dp),
                    ) {

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
private fun DeniedLayout(navigateToSetting : () -> Unit) {
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