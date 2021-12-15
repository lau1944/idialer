package com.vau.studio.iosstyle.idialer_phone.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import java.lang.Exception
import java.util.*

object OpenUtil {
    const val TAG: String = "OPEN_INTENT"

    fun openSetting(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }

    fun openSms(context: Context, phoneNumber: String?) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            if (phoneNumber.isPhoneNumber()) {
                intent.data = Uri.parse("smsto:$phoneNumber")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }

    fun openPhoneApp(context: Context, phoneNumber: String?) {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            if (phoneNumber.isPhoneNumber()) {
                intent.data = Uri.parse("tel:$phoneNumber")
                context.startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    "Phone number format error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }

    fun openEmail(context: Context, email: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.data = Uri.parse("mailto:$email")
            intent.type = "text/plain"
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }
}