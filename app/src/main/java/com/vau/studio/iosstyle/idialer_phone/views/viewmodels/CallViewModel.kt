package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.studio.iosstyle.idialer_phone.data.ALL_CALL_TYPE
import com.vau.studio.iosstyle.idialer_phone.data.models.CallHistory
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.data.repositories.PhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class CallViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneRepository: PhoneRepository
) : ViewModel() {

    private lateinit var callHistories: List<Contact>
    private val _callLogState = MutableLiveData<UiState<List<Contact>>>(UiState.InProgress)
    val callLogState: LiveData<UiState<List<Contact>>> get() = _callLogState

    private val _callLogType = MutableLiveData(ALL_CALL_TYPE)
    val callLogType: LiveData<Int> get() = _callLogType

    private val _cancelStateIndex = MutableLiveData<Int?>(null)
    val cancelStateIndex: LiveData<Int?> get() = _cancelStateIndex

    private val _isEditState = MutableLiveData<Boolean>(false)
    val isEditState: LiveData<Boolean> get() = _isEditState

    companion object {
        const val TAG: String = "CallViewModel"
    }

    /**
     * Change state of edit mode
     */
    fun changeEditState(onEdit: Boolean) {
        _isEditState.value = onEdit
    }

    /**
     * Delete item from call history list
     * If [callLog] pass as Null, we clear all the call logs by default
     */
    fun deleteHistory(callLog: Contact? = null) {
        if (_callLogState.value is UiState.Success) {
            if (callLog == null) {
                (callHistories as ArrayList<Contact>).clear()
            } else {
                phoneRepository.deleteCallLog(context, callLog.callDate)
                (callHistories as ArrayList<Contact>).remove(callLog)
            }
            val temperList = mutableListOf<Contact>().apply {
                addAll(callHistories)
            }
            _callLogState.value = UiState.Success(temperList)
        }
    }

    /**
     * Index of item where is on cancel mode
     */
    fun changeCancelState(index: Int? = null) {
        if (index == null || index < 0 || index >= callHistories.size) {
            _cancelStateIndex.value = null
        }
        _cancelStateIndex.value = index
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
            .catch { e ->
                withContext(Dispatchers.Main) {
                    Log.i(TAG, e.toString())
                    _callLogState.value = UiState.Failed(e)
                }
            }
            .collect { histories ->
                withContext(Dispatchers.Main) {
                    if (histories != null) {
                        _callLogState.value = UiState.Success(data = histories)
                        callHistories = histories
                    }
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

            val tempCallLogs = mutableListOf<Contact>().apply {
                addAll(callHistories)
            }
            val queriedList = tempCallLogs.filter { call ->
                call.type == type
            }
            _callLogState.value = UiState.Success(queriedList)
        }
    }
}