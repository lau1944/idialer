package com.vau.studio.iosstyle.idialer_phone.data.models

enum class ContactInputType {
    Phone, Mail, Address, Name, Photo;

    override fun toString() : String{
        return when (this) {
            Phone -> "phone"
            Mail -> "email"
            Address -> "address"
            Name -> "name"
            Photo -> "photo"
        }
    }
}
