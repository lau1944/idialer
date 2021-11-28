package com.vau.studio.iosstyle.idialer_phone.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val primaryId: Int? = null,
    val contactId: String = "",
    val number: String? = "",
    val name: String? = "",
    val email: String? = "",
    val phoneUrl: String? = "",
    val postal: String? = "",

    // field for call log
    val type: Int? = OUTGOING_CALL,
    val callDate: String? = "",
    val duration: String? = "",
    val location: String? = ""
) {

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