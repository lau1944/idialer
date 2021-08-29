package com.vau.studio.iosstyle.idialer_phone.core

import androidx.compose.ui.graphics.Color

object ColorUtils {
    fun parseColor(color: String) : Color {
        return Color(android.graphics.Color.parseColor(color))
    }
}