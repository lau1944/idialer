package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    companion object {
        const val TAG: String = "ContactViewModel"
    }

    /**
     * Get all contacts
     */
    @SuppressLint("Recycle")
    fun getContactNames(lookUp: String? = null) = viewModelScope.launch {
        _queriedContactState.value = UiState.InProgress

        phoneRepository.getContactNames(context, lookUp)
            .flowOn(Dispatchers.Default)
            .catch { e ->
                _queriedContactState.value = UiState.Failed(e)
            }
            .collect { contacts ->
                if (contacts != null) {
                    println(contacts)
                    _contactList.value = contacts
                    _queriedContactState.value = UiState.Success(contacts)
                } else {
                    _queriedContactState.value = UiState.Failed()
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
}