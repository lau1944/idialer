package com.vau.studio.iosstyle.idialer_phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clickCount = mutableStateOf(0)
        setContent {
            MaterialTheme() {
                Test(clickCount.value, onClick = {
                    clickCount.value++
                })
            }
        }
    }
}

@Composable
fun Test(clickCount: Int, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Button On clicked $clickCount times")
    }
}