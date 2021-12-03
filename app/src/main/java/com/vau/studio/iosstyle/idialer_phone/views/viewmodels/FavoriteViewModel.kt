package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.studio.iosstyle.idialer_phone.core.DbUtils
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState
import com.vau.studio.iosstyle.idialer_phone.data.repositories.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _contactListState = MutableLiveData<UiState<List<Contact>>>(UiState.InProgress)
    val contactListState: LiveData<UiState<List<Contact>>> get() = _contactListState

    companion object {
        const val TAG: String = "FavoriteViewModel"
    }

    init {
        getAllFavorites()
    }

    fun updateFavorite(contact: Contact) = viewModelScope.launch {
        val contacts = (_contactListState.value as UiState.Success).data
        if (hasSameContact(contact.contactId, contacts!!)) {
            val operandNumber = favoriteRepository.updateFavorite(contact = contact)
            if (DbUtils.isSuccess(operandNumber)) {
                val newContacts = mutableListOf<Contact>().apply {
                    addAll(contacts)
                }

                // update following contact
                val index = contacts.indexOfFirst { it.contactId == contact.contactId }
                if (index != -1) {
                    newContacts[index] = contact
                }

                _contactListState.value = UiState.Success(newContacts)
            }
        }
    }

    fun addToFavorite(contact: Contact) = viewModelScope.launch {
        if (_contactListState.value is UiState.Success) {
            val contacts = (_contactListState.value as UiState.Success).data
            if (!hasSameContact(contact.contactId, contacts!!)) {
                val operandNumber = favoriteRepository.addToFavorite(contact = contact)
                if (DbUtils.isSuccess(operandNumber.toInt())) {
                    val newContacts: List<Contact> = mutableListOf<Contact>().apply {
                        add(contact)
                        addAll(1, contacts)
                    }
                    _contactListState.value = UiState.Success(newContacts)
                }
            }
        }
    }

    fun deleteFavorite(contact: Contact) = viewModelScope.launch {
        favoriteRepository.deleteFavorite(contact)
        val contacts = (_contactListState.value as UiState.Success).data
        if (hasSameContact(contact.contactId, contacts!!)) {
            val newContacts: List<Contact> = mutableListOf<Contact>().apply {
                addAll(contacts)
                remove(contact)
            }
            _contactListState.value = UiState.Success(newContacts)
        }
    }

    fun deleteAll() = viewModelScope.launch {
        val operandNumber = favoriteRepository.deleteAllFavorite()
        if (DbUtils.isSuccess(operandNumber = operandNumber)) {
            _contactListState.value = UiState.Success(mutableListOf())
        }
    }

    fun exist(id: Int?) : Boolean {
        if (id == null || id == 0) return false

        if (_contactListState.value is UiState.Success) {
            val contacts = (_contactListState.value as UiState.Success<List<Contact>>).data
            return contacts?.any {
                it.contactId == id
            } ?: false
        }

        return false
    }

    private fun getAllFavorites() = viewModelScope.launch {
        favoriteRepository.getAllFavorite()
            .catch { e ->
                withContext(Dispatchers.Main) {
                    Log.i(TAG, e.toString())
                    _contactListState.value = UiState.Failed(exception = e)
                }
            }
            .collect { contacts ->
                withContext(Dispatchers.Main) {
                    _contactListState.value = UiState.Success(contacts)
                }
            }
    }

    private fun hasSameContact(contactId: Int?, contacts: List<Contact>): Boolean {
        return contacts.any {
            it.contactId == contactId
        }
    }
}