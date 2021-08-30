package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialerViewModel : ViewModel() {

    private val _inputNumber = MutableLiveData<List<String>>(arrayListOf())
    val inputNumber : LiveData<List<String>> get() = _inputNumber

    fun appendNumber(number: String) {
        val numberList = _inputNumber.value!!
        if (numberList is ArrayList) {
            _inputNumber.value = listOf(number) + numberList
        } else {
            throw IllegalArgumentException("Input number list should be array list type")
        }
    }

    fun removeNumber(number: String) {
        val numberList = _inputNumber.value!!
        if (numberList is ArrayList) {
            _inputNumber.value = numberList - listOf(number)
        } else {
            throw IllegalArgumentException("Input number list should be array list type")
        }
    }
}