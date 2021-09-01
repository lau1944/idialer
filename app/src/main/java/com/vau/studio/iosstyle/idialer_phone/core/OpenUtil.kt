package com.vau.studio.iosstyle.idialer_phone.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object OpenUtil {
    fun openSetting(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}