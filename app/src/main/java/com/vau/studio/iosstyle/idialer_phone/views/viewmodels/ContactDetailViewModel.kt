package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.studio.iosstyle.idialer_phone.data.QUERY_CONTACT_BY_ID
import com.vau.studio.iosstyle.idialer_phone.data.QUERY_CONTACT_BY_NUMBER
import com.vau.studio.iosstyle.idialer_phone.data.QUERY_LOG_BY_NUMBER
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.data.repositories.PhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ContactDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneRepository: PhoneRepository
) : ViewModel() {

    private val _contactDetail = MutableLiveData<UiState<Contact>>()
    val contactDetail: LiveData<UiState<Contact>> get() = _contactDetail

    init {
        initState()
    }

    fun getContactDetailById(id: String) = viewModelScope.launch {
        phoneRepository.getContactNames(context, QUERY_CONTACT_BY_ID + id)
            .flowOn(Dispatchers.Main)
            .collect { contacts ->
                if (!contacts.isNullOrEmpty()) {
                    _contactDetail.value = UiState.Success(contacts.first())
                }
            }
    }

    fun getContactDetailByNumber(number: String) = viewModelScope.launch {
        phoneRepository.getContactNames(context, QUERY_CONTACT_BY_NUMBER + number)
            .flowOn(Dispatchers.Main)
            .collect { contacts ->
                if (!contacts.isNullOrEmpty()) {
                    _contactDetail.value = UiState.Success(contacts.first())
                }
            }
    }

    fun getCallLogByNumber(number: String) = viewModelScope.launch {
        phoneRepository.getCallLog(context, null, QUERY_LOG_BY_NUMBER + number)
            .flowOn(Dispatchers.Main)
            .collect { contacts ->
                if (!contacts.isNullOrEmpty()) {
                    _contactDetail.value = UiState.Success(contacts.first())
                }
            }
    }

    fun initState() {
        _contactDetail.value = UiState.InProgress
    }

}