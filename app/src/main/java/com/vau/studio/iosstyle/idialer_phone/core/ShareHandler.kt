package com.vau.studio.iosstyle.idialer_phone.core

import android.content.Context
import android.content.Intent

object ShareHandler {

    fun shareText(context: Context, text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(sendIntent)
    }
}