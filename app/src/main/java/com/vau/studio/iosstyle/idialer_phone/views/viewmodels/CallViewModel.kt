package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.studio.iosstyle.idialer_phone.data.ALL_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.data.repositories.PhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class CallViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneRepository: PhoneRepository
) : ViewModel() {

    private lateinit var callHistories: List<CallHistory>
    private val _callLogState = MutableLiveData<UiState<List<CallHistory>>>(UiState.InProgress)
    val callLogState: LiveData<UiState<List<CallHistory>>> get() = _callLogState

    private val _callLogType = MutableLiveData(ALL_CALL_TYPE)
    val callLogType : LiveData<Int> get() = _callLogType

    private val _cancelStateItem = MutableLiveData<CallHistory>(null)
    val cancelStateItem : LiveData<CallHistory?> get() = _cancelStateItem

    companion object {
        const val TAG: String = "CallViewModel"
    }

    /**
     * Delete item from call history list
     */
    fun deleteHistory(callHistory: CallHistory) = viewModelScope.launch {
        (callHistories as ArrayList<CallHistory>).remove(callHistory)
        phoneRepository.deleteCallLog(context, callHistory.date)
        _callLogState.value = UiState.Success(callHistories)
    }

    /**
     * Index of item where is on cancel mode
     */
    fun changeCancelState(callHistory: CallHistory) {
        _cancelStateItem.value = callHistory
    }

    /**
     * Get all the call history from Call Api
     */
    fun getCallHistory() = viewModelScope.launch {
        phoneRepository.getCallLog(
            context, arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
            )
        )
            .flowOn(Dispatchers.Default)
            .catch { e ->
                Log.i(TAG, e.toString())
                _callLogState.value = UiState.Failed(e)
            }
            .collect { histories ->
            if (histories != null) {
                _callLogState.value = UiState.Success(data = histories)
                callHistories = histories
            }
        }
    }

    fun queryByType(type: Int) {
        if (type == _callLogType.value) return

        _callLogType.value = type
        if (_callLogState.value is UiState.Success) {
            if (type == ALL_CALL_TYPE) {
                _callLogState.value = UiState.Success(callHistories)
                return
            }

            val tempCallLogs = mutableListOf<CallHistory>().apply {
                addAll(callHistories)
            }
            val queriedList = tempCallLogs.filter { call ->
                call.type == type
            }
            _callLogState.value = UiState.Success(queriedList)
        }
    }
}