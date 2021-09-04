package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.data.repositories.PhoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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

    private val _queriedContactState = MutableLiveData<UiState<List<String>>>()
    private val _contactList = MutableLiveData<List<String>>()
    val contactListState: LiveData<UiState<List<String>>> get() = _queriedContactState

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
            .collect { contacts ->
            if (contacts != null) {
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
                _queriedContactState.value = state.copy(it?.filter { name ->
                    name.startsWith(query!!)
                })
            }
        }
    }
}