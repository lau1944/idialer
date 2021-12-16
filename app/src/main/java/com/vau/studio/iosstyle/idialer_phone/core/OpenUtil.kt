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
    private const val TAG: String = "OPEN_INTENT"

    fun openMyApp(context: Context, packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
                .apply {
                    data = Uri.parse("market://details?id=$packageName")
                }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }

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
                ToastUtil.make(context, "Phone number format error")
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