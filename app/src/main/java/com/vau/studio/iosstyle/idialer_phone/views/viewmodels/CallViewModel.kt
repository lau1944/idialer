package com.vau.studio.iosstyle.idialer_phone.views.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class CallViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {


}