package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialerViewModel : ViewModel() {

    private val _inputNumber = MutableLiveData<List<String>>(arrayListOf())
    val inputNumber: LiveData<List<String>> get() = _inputNumber

    fun numberListToString(): String {
        val buffer = StringBuffer()
        for (number: String in _inputNumber.value!!) {
            buffer.append(number)
        }
        return buffer.toString()
    }

    fun appendNumber(number: String) {
        val numberList = _inputNumber.value!!
        if (numberList is ArrayList) {
            _inputNumber.value = numberList + number
        } else {
            throw IllegalArgumentException("Input number list should be array list type")
        }
    }

    fun removeNumber(number: String? = null) {
        val numberList = _inputNumber.value!!
        if (numberList is ArrayList) {
            if (number.isNullOrEmpty())
                _inputNumber.value = numberList - numberList.last()
            else
                _inputNumber.value = numberList - number
        } else {
            throw IllegalArgumentException("Input number list should be array list type")
        }
    }

    fun clearAllNumber() {
        _inputNumber.value = arrayListOf()
    }
}