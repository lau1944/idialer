package com.vau.studio.iosstyle.idialer_phone.data.models

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.lang.Exception
import java.util.*

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val contactId: Int? = null,
    val number: String? = "",
    val name: String? = "",
    val email: String? = "",
    val phoneUrl: String? = "",
    val postal: String? = "",

    // field for call log
    val type: Int? = OUTGOING_CALL,
    val callDate: String? = "",
    val duration: String? = "",
    val location: String? = "",
) {

    @Ignore
    private var photoBitmap: Bitmap? = null

    fun setPhotoBitmap(bitmap: Bitmap?) {
        this.photoBitmap = bitmap
    }

    fun getPhotoBitmap() : Bitmap? = this.photoBitmap

    fun getFieldFromType(type: ContactInputType?): String? {
        if (type == null) return ""

        return when (type) {
            ContactInputType.Address -> {
                location
            }
            ContactInputType.Mail -> {
                email
            }
            ContactInputType.Name -> {
                name
            }
            ContactInputType.Phone -> {
                number?.toString()
            }
            else -> ""
        }
    }

}