package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.repositories.PhoneRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class ContactDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneRepository: PhoneRepository
) : ViewModel() {

    companion object {
        const val QUERY_BY_NUMBER = "${ContactsContract.CommonDataKinds.Phone.NUMBER}="
    }

    private val _contactDetail = MutableLiveData<Contact>()
    val contactDetail: LiveData<Contact> get() = _contactDetail

    fun getContactDetailByNumber(number: String) = viewModelScope.launch {
        phoneRepository.getContactNames(context, QUERY_BY_NUMBER + number)
            .flowOn(Dispatchers.Main)
            .collect { contacts ->
                if (!contacts.isNullOrEmpty()) {
                    _contactDetail.value = contacts.first()
                }
            }
    }

}