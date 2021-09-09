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
    alignment: Alignment? = Alignment.Center,
    loadingState: @Composable (() -> Unit)? = null,
    errorState: @Composable (() -> Unit)? = null,
    successState: @Composable () -> Unit,
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
        is UiState.Success -> {
            successState.invoke()
        }
    }
}

@Composable
private fun ProgressBox(alignment: Alignment? = Alignment.Center, layout: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment!!
    ) {
        layout()
    }
}