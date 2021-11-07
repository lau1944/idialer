package com.vau.studio.iosstyle.idialer_phone.data.models

sealed class ContactPageType {
    object Recent : ContactPageType()
    object Contact : ContactPageType()

    companion object {
        fun map(typeStr: String?): ContactPageType? {
            if (typeStr.isNullOrEmpty()) return null

            return when (typeStr) {
                "Recents" -> Recent
                "Contact" -> Contact
                else -> null
            }
        }
    }
}
