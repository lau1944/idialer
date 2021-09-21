package com.vau.studio.iosstyle.idialer_phone.views.composable.recent_screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.vau.studio.iosstyle.idialer_phone.core.OpenUtil
import com.vau.studio.iosstyle.idialer_phone.data.ALL_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.CALL_LOG_READ_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.CALL_LOG_WRITE_PERMISSION
import com.vau.studio.iosstyle.idialer_phone.data.MISSED_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.appColor
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.DeniedLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen.CallLogItem
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.CallViewModel

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun RecentUi(
    callViewModel: CallViewModel
) {
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
                    queryCallType(callViewModel, i)
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
                    CallList(histories = callLogs!!, callViewModel = callViewModel)
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

@ExperimentalMaterialApi
@Composable
private fun CallList(histories: List<CallHistory>, callViewModel: CallViewModel) {
    if (histories.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No call history")
        }
    } else {
        val onDragItem = callViewModel.cancelStateItem.observeAsState()

        LazyColumn(
            content = {
                items(histories.size + 1) { i ->
                    if (i == 0) {
                        Text(
                            "Recents",
                            style = TextStyle(
                                color = appColor().surface,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(10.dp)
                        )
                    } else {
                        val callLogIndex = i - 1
                        CallLogItem(
                            callHistory = histories[callLogIndex],
                            callViewModel = callViewModel,
                            onDrag = onDragItem.value == histories[callLogIndex],
                            onDelete = { callHistory ->
                                callViewModel.deleteHistory(callHistory = callHistory)
                            }
                        )
                    }
                }
            },
            verticalArrangement = Arrangement.Top
        )
    }
}

private fun queryCallType(callViewModel: CallViewModel, index: Int) {
    if (index == 1) {
        callViewModel.queryByType(MISSED_CALL_TYPE)
    } else {
        callViewModel.queryByType(ALL_CALL_TYPE)
    }
}