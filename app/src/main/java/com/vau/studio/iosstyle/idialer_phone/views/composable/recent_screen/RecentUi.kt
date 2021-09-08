package com.vau.studio.iosstyle.idialer_phone.views.composable.recent_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.vau.studio.iosstyle.idialer_phone.core.OpenUtil
import com.vau.studio.iosstyle.idialer_phone.data.CALL_LOG_READ_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.CALL_LOG_WRITE_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.DeniedLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen.CallLogItem
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.CallViewModel

@ExperimentalPermissionsApi
@Composable
fun RecentUi(
    callViewModel: CallViewModel
) {
    val callLogType = remember { mutableStateOf(0) }
    val callLogState = callViewModel.callLogState.observeAsState()
    val context = LocalContext.current

    val callLogPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            CALL_LOG_READ_PERMISSION,
            CALL_LOG_WRITE_PERMISSION
        )
    )

    Scaffold(
        topBar = {
            RecentAppBar(
                onSelected = { i ->
                    callLogType.value = i
                }
            )
        }
    ) {

        when {
            callLogPermissionState.allPermissionsGranted -> {
                LaunchedEffect(true) {
                    callViewModel.getCallHistory()
                }

                UiProgressLayout(state = callLogState.value) {
                    val callLogs = (callLogState.value as UiState.Success).data
                    CallList(histories = callLogs!!)
                }
            }

            callLogPermissionState.shouldShowRationale ||
                    !callLogPermissionState.allPermissionsGranted -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(onClick = {
                        callLogPermissionState.launchMultiplePermissionRequest()
                    }) {
                        Text(
                            "Grant Call Log Permission", style = TextStyle(
                                appColor().background,
                                fontSize = 16.sp,
                            )
                        )
                    }
                }
            }

            else -> {
                DeniedLayout {
                    OpenUtil.openSetting(context)
                }
            }
        }
    }
}

@Composable
private fun CallList(histories: List<CallHistory>) {
    if (histories.isEmpty()) {
        Text("No call history")
    }

    LazyColumn(
        content = {
            items(histories.size) { i ->
                CallLogItem(callHistory = histories[i])
            }
        },
        verticalArrangement = Arrangement.Top
    )
}