package com.vau.studio.iosstyle.idialer_phone.data.models

import java.lang.Exception
import java.util.*

sealed class UiState<out T: Any> {
    object InProgress : UiState<Any>()
    data class Failed(val exception: Exception): UiState<Any>()
    data class Success<out T: Any>(val data: T): UiState<T>()
}