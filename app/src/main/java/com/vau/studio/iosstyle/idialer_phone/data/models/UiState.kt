package com.vau.studio.iosstyle.idialer_phone.data.models

import java.lang.Exception
import java.util.*

sealed class UiState<out T: Any> {
    object InProgress : UiState<Nothing>()
    data class Failed(val exception: Throwable? = null): UiState<Nothing>()
    data class Success<out T: Any>(val data: T?): UiState<T>() {
        @JvmName("copy_success")
        fun <T: Any> copy(data: T) : Success<T> {
            return Success(data)
        }
    }
}