package com.vau.studio.iosstyle.idialer_phone.core

import android.content.Context
import android.widget.Toast

class ToastUtil {
    companion object {
        fun make(context: Context, text: String) {
            Toast.makeText(
                context,
                text,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}