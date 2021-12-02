package com.vau.studio.iosstyle.idialer_phone.data.models

sealed class UiState<out T: Any> {
    object InIdle: UiState<Nothing>()
    object InProgress : UiState<Nothing>()
    data class Failed(val exception: Throwable? = null): UiState<Nothing>()
    data class Success<T: Any>(var data: T? = null): UiState<T>() {
        @JvmName("copy_success")
        fun <T: Any> copy(data: T) : Success<T> {
            return Success(data)
        }
    }
}