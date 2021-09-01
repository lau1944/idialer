/*
package com.vau.studio.iosstyle.idialer_phone.core

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHandler {
    private val grantedPermission = mutableSetOf<String>()

    fun requestPermission(
        context: Activity,
        permissionCode: String,
        requestCode: Int,
        permissionGranted: () -> Unit
    ) {
        if (hasPermission(context = context, permissionCode = permissionCode)) {
            permissionGranted()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(arrayOf(permissionCode), requestCode)
        } else {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(permissionCode),
                requestCode
            )
        }
    }

    private fun addToPermissionSet(permissionCode: String) {
        grantedPermission.add(permissionCode)
    }

    private fun hasPermission(context: Context, permissionCode: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permissionCode
        ) == PackageManager.PERMISSION_GRANTED
    }
}
*/
