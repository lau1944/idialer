package com.vau.studio.iosstyle.idialer_phone.data.models

data class Contact(
    val number: String? = "",
    val name: String? = "",
    val email: String? = "",
    val contactId: String? = "",
    val phoneUrl: String? = ""
) {
    override fun toString(): String {
        return "Contact Info: \n number: $number, " +
                "name: $name, email: $email, " +
                "contactId: $contactId. phoneUrl: $phoneUrl"
    }
}