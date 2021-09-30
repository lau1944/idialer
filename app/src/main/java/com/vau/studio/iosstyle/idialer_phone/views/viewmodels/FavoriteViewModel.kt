package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

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

    fun getAllFavorites() = viewModelScope.launch {
        favoriteRepository.getAllFavorite().flowOn(Dispatchers.Default)
            .catch { e ->
                _contactListState.value = UiState.Failed(exception = e)
            }
            .collect { contacts ->
                _contactListState.value = UiState.Success(contacts)
            }
    }

    fun updateFavorite(contact: Contact) = viewModelScope.launch {
        val operandNumber = favoriteRepository.updateFavorite(contact = contact)
        if (DbUtils.isSuccess(operandNumber)) {
            val contacts = (_contactListState.value as UiState.Success).data
            val newContacts = mutableListOf<Contact>().apply {
                addAll(contacts!!)
            }

            // update following contact
            val index = contacts!!.indexOfFirst { it.contactId == contact.contactId }
            if (index != -1) {
                newContacts[index] = contact
            }

            _contactListState.value = UiState.Success(newContacts)
        }
    }

    fun addToFavorite(contact: Contact) = viewModelScope.launch {
        if (_contactListState.value is UiState.Success) {
            val operandNumber = favoriteRepository.addToFavorite(contact = contact)
            if (DbUtils.isSuccess(operandNumber)) {
                val contacts = (_contactListState.value as UiState.Success).data
                val newContacts: List<Contact> = mutableListOf<Contact>().apply {
                    add(contact)
                    addAll(contacts!!)
                }
                _contactListState.value = UiState.Success(newContacts)
            }
        }
    }

    fun deleteFavorite(contact: Contact) = viewModelScope.launch {
        val operandNumber = favoriteRepository.deleteFavorite(contact)
        if (DbUtils.isSuccess(operandNumber)) {
            val contacts = (_contactListState.value as UiState.Success).data
            val newContacts: List<Contact> = mutableListOf<Contact>().apply {
                addAll(contacts!!)
                remove(contact)
            }
            _contactListState.value = UiState.Success(newContacts)
        }
    }

    fun deleteAll() = viewModelScope.launch {
        val operandNumber = favoriteRepository.deleteAllFavorite()
        if (DbUtils.isSuccess(operandNumber = 0)) {
            _contactListState.value = UiState.Success(mutableListOf())
        }
    }
}