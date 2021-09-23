package com.vau.studio.iosstyle.idialer_phone.data.models

sealed class UiState<out T: Any> {
    object InProgress : UiState<Nothing>()
    data class Failed(val exception: Throwable? = null): UiState<Nothing>()
    data class Success<T: Any>(var data: T?): UiState<T>() {
        @JvmName("copy_success")
        fun <T: Any> copy(data: T) : Success<T> {
            return Success(data)
        }
    }
}