package com.vau.studio.iosstyle.idialer_phone.views.composable.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vau.studio.iosstyle.idialer_phone.data.models.UiState

@Composable
fun UiProgressLayout(
    state: UiState<Any>?,
    loadingState: @Composable (() -> Unit)? = null,
    errorState: @Composable (() -> Unit)? = null,
    successState: @Composable () -> Unit
) {
    if (state == null) ProgressBox {
        CircularProgressIndicator()
    }

    when (state) {
        is UiState.InProgress -> ProgressBox {
            loadingState?.invoke() ?: CircularProgressIndicator()
        }
        is UiState.Failed -> ProgressBox {
            errorState?.invoke() ?: Text("Something went wrong")
        }
        is UiState.Success -> ProgressBox(false) {
            successState.invoke()
        }
    }
}

@Composable
private fun ProgressBox(isCenter: Boolean? = true, layout: @Composable () -> Unit) {
    val alignment: Alignment = if (isCenter!!) Alignment.Center else Alignment.TopStart
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        layout()
    }
}