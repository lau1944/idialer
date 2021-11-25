package com.vau.studio.iosstyle.idialer_phone.data.models

import android.util.Log
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.Exception
import java.lang.NumberFormatException

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val number: Long? = 0,
    val contactId: String = "",
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
                number.toString()
            }
            else -> ""
        }
    }

}