package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
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
import contacts.core.Fields
import contacts.core.equalTo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ContactDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneRepository: PhoneRepository
) : ViewModel() {

    private val _contactDetail = MutableLiveData<UiState<Contact>>()
    val contactDetail: LiveData<UiState<Contact>> get() = _contactDetail

    private val _newContact = MutableLiveData<Contact>(Contact())
    val newContact: LiveData<Contact> get() = _newContact

    private val _contactAddResultState = MutableLiveData<UiState<Contact>>(UiState.InIdle)
    val contactAddResultState: LiveData<UiState<Contact>> get() = _contactAddResultState

    private val _contactUpdateState = MutableLiveData<UiState<Contact>>(UiState.InIdle)
    val contactUpdateState: LiveData<UiState<Contact>> get() = _contactUpdateState

    private val _contactDeleteState = MutableLiveData<UiState<Any>>(UiState.InIdle)
    val contactDeleteState: LiveData<UiState<Any>> get() = _contactDeleteState

    init {
        initState()
    }

    fun createContact() = viewModelScope.launch {
        val contact = _newContact.value

        if (contact != null) {
            updateContactCreateState(UiState.InProgress)
            phoneRepository.createNewContact(context, contact = contact)
                .catch {
                    Log.i("ContactDetailViewModel", this.toString())
                }
                .collect { result ->
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            updateContactCreateState(UiState.Success(null))
                        } else {
                            updateContactCreateState(UiState.Failed())
                        }
                    }
                }
        }
        _newContact.value = Contact()
    }

    fun deleteContact(contactId: String) = viewModelScope.launch {
        phoneRepository.deleteContactById(context, contactId)
            .collect {
                withContext(Dispatchers.Main) {
                    _contactDeleteState.value = it
                }
            }
    }

    fun updateContactCreateState(state: UiState<Contact>) {
        _contactAddResultState.value = state
    }

    fun modifyNewContact(contact: Contact) {
        _newContact.value = contact
    }

    /**
     * Update current contact info
     */
    fun updateContact(contact: Contact? = _newContact.value) = viewModelScope.launch {
        if (_newContact.value != null) {
            _contactUpdateState.value = UiState.InProgress
            phoneRepository.updateContact(context, _newContact.value!!)
                .catch {
                    _contactUpdateState.value = UiState.Failed()
                }
                .collect {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            _contactUpdateState.value = UiState.Success()
                            _contactDetail.value = UiState.Success(_newContact.value)
                        } else {
                            _contactUpdateState.value = UiState.Failed()
                        }
                    }
                }
        }
    }

    fun getContactDetailById(id: Int) = viewModelScope.launch {
        phoneRepository.getContactNames(context, (Fields.Contact.Id equalTo id.toLong()))
            .collect { contacts ->
                withContext(Dispatchers.Main) {
                    if (!contacts.isNullOrEmpty()) {
                        _contactDetail.value = UiState.Success(contacts.first())
                        //updateContact(contacts.first())
                    }
                }
            }
    }

    fun getContactDetailByNumber(number: String) = viewModelScope.launch {
        phoneRepository.getContactNames(context, (Fields.Contact.DisplayNamePrimary equalTo number))
            .collect { contacts ->
                withContext(Dispatchers.Main) {
                    if (!contacts.isNullOrEmpty()) {
                        _contactDetail.value = UiState.Success(contacts.first())
                        //updateContact(contacts.first())
                    }
                }
            }
    }

    fun getCallLogByNumber(number: String) = viewModelScope.launch {
        phoneRepository.getCallLog(context, null, QUERY_LOG_BY_NUMBER + number)
            .collect { contacts ->
                withContext(Dispatchers.Main) {
                    if (!contacts.isNullOrEmpty()) {
                        _contactDetail.value = UiState.Success(contacts.first())
                        //updateContact(contacts.first())
                    }
                }
            }
    }

    fun initState() {
        _contactDetail.value = UiState.InProgress
        _contactUpdateState.value = UiState.InIdle
        _contactAddResultState.value = UiState.InIdle
        _contactDeleteState.value = UiState.InIdle
        _newContact.value = Contact()
    }

}