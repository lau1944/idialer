package com.vau.studio.iosstyle.idialer_phone.core

const val VALID_NUMBER =
    "^\\+?\\(?[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})?"

fun String?.isPhoneNumber(): Boolean {
    return this?.matches(VALID_NUMBER.toRegex()) ?: false
}