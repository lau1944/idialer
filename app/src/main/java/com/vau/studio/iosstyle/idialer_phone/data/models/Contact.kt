package com.vau.studio.iosstyle.idialer_phone.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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
)