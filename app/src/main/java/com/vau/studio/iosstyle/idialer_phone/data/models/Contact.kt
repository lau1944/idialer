package com.vau.studio.iosstyle.idialer_phone.data.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

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

    fun getPhotoBitmap(): Bitmap? = this.photoBitmap

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

    /**
     * Use this method to clone object instead of copy
     */
    fun clone(
        contactId: Int? = this.contactId,
        number: String? = this.number,
        name: String? = this.name,
        email: String? = this.email,
        phoneUrl: String? = this.phoneUrl,
        postal: String? = this.postal,
        type: Int? = this.type,
        callDate: String? = this.callDate,
        duration: String? = this.duration,
        location: String? = this.location
    ): Contact {
        val newContact = Contact(
            contactId,
            number,
            name,
            email,
            phoneUrl,
            postal,
            type,
            callDate,
            duration,
            location
        )
        if (this.photoBitmap != null) {
            newContact.setPhotoBitmap(photoBitmap)
        }
        return newContact
    }

}