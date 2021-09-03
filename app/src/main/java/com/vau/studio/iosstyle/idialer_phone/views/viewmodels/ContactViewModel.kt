package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.core.database.getStringOrNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ContactViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _queriedContacts = MutableLiveData<List<String>>(mutableListOf())
    private val _contactList = MutableLiveData<List<String>>(mutableListOf())
    val contactList: LiveData<List<String>> get() = _queriedContacts

    companion object {
        const val TAG: String = "ContactViewModel"
    }

    /**
     * Get all contacts
     */
    @SuppressLint("Recycle")
    fun getContactNames(lookUp: String? = null) = viewModelScope.launch {
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
            _queriedContacts.value = contacts
            cursor.close()
        }
    }

    /**
     * Query contact by name on [_queriedContacts]
     */
    fun queryNumberByName(query: String?) {
        if (query.isNullOrEmpty()) _queriedContacts.value = _contactList.value

        val contacts = _contactList.value
        contacts.let {
            _queriedContacts.value = it?.filter { name ->
                name.startsWith(query!!)
            }
        }
    }
}