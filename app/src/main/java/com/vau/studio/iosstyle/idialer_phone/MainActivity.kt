package com.vau.studio.iosstyle.idialer_phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vau.studio.iosstyle.idialer_phone.views.composable.AppTheme
import com.vau.studio.iosstyle.idialer_phone.views.composable.home_screen.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(isDarkTheme = false) {
                HomeScreen()
            }
        }
    }
}