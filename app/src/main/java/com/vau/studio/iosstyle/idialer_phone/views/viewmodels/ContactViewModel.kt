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
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ContactViewModel @Inject constructor(
    @ApplicationContext private val context: Context
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
        val cursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ),
            lookUp,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor != null) {
            val contacts = mutableListOf<String>()
            while (cursor.moveToNext()) {
                val nameIndex =
                    cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    )
                if (nameIndex > -1) {
                    contacts.add(cursor.getStringOrNull(nameIndex) ?: "")
                }
            }
            // submit names for all contacts info
            _contactList.value = contacts
            // submit names for query purposes
            _queriedContactState.value = UiState.Success(data = contacts)
            cursor.close()
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