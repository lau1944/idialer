package com.vau.studio.iosstyle.idialer_phone.views.composable.recent_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.views.composable.components.UiProgressLayout
import com.vau.studio.iosstyle.idialer_phone.views.composable.keypad_screen.CallLogItem
import com.vau.studio.iosstyle.idialer_phone.views.viewmodels.CallViewModel

@Composable
fun RecentUi(
    callViewModel: CallViewModel
) {
    val callLogType = remember { mutableStateOf(0) }
    val callLogState = callViewModel.callLogState.observeAsState()

    Scaffold(
        topBar = {
            RecentAppBar(
                onSelected = { i ->
                    callLogType.value = i
                }
            )
        }
    ) {

        UiProgressLayout(state = callLogState.value) {
            val callLogs = (callLogState.value as UiState.Success).data
            CallList(histories = callLogs!!)
        }
    }
}

@Composable
private fun CallList(histories: List<CallHistory>) {
    LazyColumn(content = {
        items(histories.size) { i ->
            CallLogItem(callHistory = histories[i])
        }
    },
        verticalArrangement = Arrangement.Top)
}