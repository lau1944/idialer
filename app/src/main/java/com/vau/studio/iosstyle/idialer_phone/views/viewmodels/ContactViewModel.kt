package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.data.repositories.PhoneRepository
import contacts.core.AbstractDataField
import contacts.core.Where
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
class ContactViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneRepository: PhoneRepository
) : ViewModel() {

    private val _queriedContactState = MutableLiveData<UiState<List<Contact>>>()
    private val _contactList = MutableLiveData<List<Contact>>()
    val contactListState: LiveData<UiState<List<Contact>>> get() = _queriedContactState

    private val _blockNumbers = MutableLiveData<List<String>>()
    val blockNumbers: LiveData<List<String>> get() = _blockNumbers

    companion object {
        const val TAG: String = "ContactViewModel"
    }

    init {
        getContactNames()
    }

    /**
     * Get all contacts
     */
    @SuppressLint("Recycle")
    fun getContactNames(where: Where<AbstractDataField>? = null) = viewModelScope.launch {
        _queriedContactState.value = UiState.InProgress

        phoneRepository.getContactNames(context, where)
            .catch { e ->
               withContext(Dispatchers.Main) {
                   _queriedContactState.value = UiState.Failed(e)
               }
            }
            .collect { contacts ->
                withContext(Dispatchers.Main) {
                    if (contacts != null) {
                        _contactList.value = contacts
                        _queriedContactState.value = UiState.Success(contacts)
                    } else {
                        _queriedContactState.value = UiState.Failed()
                    }
                }
            }
    }

    /**
     * Query contact by name on [_queriedContactState]
     */
    fun queryNumberByName(query: String?) {
        val state = _queriedContactState.value
        if (state is UiState.Success) {
            val contacts = _contactList.value
            if (query.isNullOrEmpty()) _queriedContactState.value =
                state.copy(data = contacts)

            contacts.let {
                _queriedContactState.value = state.copy(it?.filter { contact ->
                    contact.name?.startsWith(query!!) ?: false
                })
            }
        }
    }

    /**
     * Check if contact exist in the list
     */
    fun existInContact(id: Int?): Boolean {
        if (id == null || id == 0) return false

        val contacts = _contactList.value
        return contacts?.any {
            it.contactId == id
        } ?: false
    }


    /**
     * Get all block number
     */
    fun getBlockNumbers() = viewModelScope.launch {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            phoneRepository.getBlockNumber(context)
                .collect { numbers ->
                   withContext(Dispatchers.Main) {
                       _blockNumbers.value = numbers
                   }
                }
        }
    }

    fun addBlockNumber(number: String) = viewModelScope.launch {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            phoneRepository.addBlockNumber(context, number)
            updateBlockList(number, true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun removeBlockNumber(number: String) = viewModelScope.launch {
        phoneRepository.deleteBlockNumber(context, number)
        updateBlockList(number, false)
    }

    fun isInBlock(number: String): Boolean {
        val numbers = blockNumbers.value
        return if (numbers.isNullOrEmpty()) false else numbers.contains(number)
    }

    private fun updateBlockList(number: String, isAdd: Boolean) {
        val blocked = _blockNumbers.value
        val newBlocked = arrayListOf<String>().apply {
            if (blocked != null) {
                addAll(blocked)

                if (isAdd) {
                    add(number)
                } else if (blocked.contains(number)){
                    this.remove(number)
                }
            }
        }
        _blockNumbers.value = newBlocked
    }
}