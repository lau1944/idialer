package com.vau.studio.iosstyle.idialer_phone.core

const val VALID_NUMBER =
    "^[0-9]+\$"

fun String?.isPhoneNumber(): Boolean {
    return this?.matches(VALID_NUMBER.toRegex()) ?: false
}