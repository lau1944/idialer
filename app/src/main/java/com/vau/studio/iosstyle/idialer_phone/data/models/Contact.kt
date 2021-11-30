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
        contactId: Int? = null,
        number: String? = null,
        name: String? = null,
        email: String? = null,
        phoneUrl: String? = null,
        postal: String? = null,
        type: Int? = null,
        callDate: String? = null,
        duration: String? = null,
        location: String? = null
    ): Contact {
        val newContact = Contact(
            contactId ?: this.contactId,
            number ?: this.number,
            name ?: this.name,
            email ?: this.email,
            phoneUrl ?: this.phoneUrl,
            postal ?: this.postal,
            type ?: this.type,
            callDate ?: this.callDate,
            duration ?: this.duration,
            location ?: this.location
        )
        if (this.photoBitmap != null) {
            newContact.setPhotoBitmap(photoBitmap)
        }
        return newContact
    }

}