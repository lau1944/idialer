package com.vau.studio.iosstyle.idialer_phone.views.composable.recent_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.SelectionDialog
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.SelectionOption
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen.CallLogItem
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.CallViewModel

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun RecentUi(
    callViewModel: CallViewModel
) {
    val callLogState by callViewModel.callLogState.observeAsState()
    val onEdit by callViewModel.isEditState.observeAsState(false)
    val context = LocalContext.current

    val showClearDialog = remember {
        mutableStateOf(false)
    }

    if (showClearDialog.value) {
        SelectionDialog(
            options = listOf(
                SelectionOption(
                    "Clear All Recents",
                    onTap = {
                        showClearDialog.value = false
                        callViewModel.deleteHistory()
                        callViewModel.changeEditState(false)
                    }
                ),
            ),
            onDismiss = {
                showClearDialog.value = false
            }
        )
    }

    val callLogPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            CALL_LOG_READ_PERMISSION,
            CALL_LOG_WRITE_PERMISSION
        )
    )

    Scaffold(
        topBar = {
            RecentAppBar(
                onEditMode = onEdit,
                onSelected = { i ->
                    queryCallType(callViewModel, i)
                },
                onEdit = {
                    callViewModel.changeEditState(it)
                },
                onClear = {
                    showClearDialog.value = true
                }
            )
        }
    ) {

        when {
            callLogPermissionState.allPermissionsGranted -> {
                LaunchedEffect(true) {
                    callViewModel.getCallHistory()
                }

                UiProgressLayout(state = callLogState) {
                    val callLogs = (callLogState as UiState.Success).data
                    CallList(
                        histories = callLogs!!,
                        callViewModel = callViewModel,
                        onEdit = onEdit
                    )
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
private fun CallList(histories: List<CallHistory>, callViewModel: CallViewModel, onEdit: Boolean) {
    val onEditItemIndex by callViewModel.cancelStateIndex.observeAsState()

    if (histories.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No call history")
        }
    } else {
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
                            isOnDeleteMode = onEditItemIndex == callLogIndex,
                            onTap = {
                                callViewModel.changeCancelState()
                            },
                            onDrag = {
                                callViewModel.changeCancelState(histories.indexOf(it))
                            },
                            onEdit = onEdit,
                            onDelete = { callHistory ->
                                callViewModel.changeCancelState()
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