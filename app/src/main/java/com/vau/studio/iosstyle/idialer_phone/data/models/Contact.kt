package com.vau.studio.iosstyle.idialer_phone.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val contactId: String? = "",
    val number: String? = "",
    val name: String? = "",
    val email: String? = "",
    val phoneUrl: String? = ""
) {
    override fun toString(): String {
        return "Contact Info: \n number: $number, " +
                "name: $name, email: $email, " +
                "contactId: $contactId. phoneUrl: $phoneUrl"
    }
}