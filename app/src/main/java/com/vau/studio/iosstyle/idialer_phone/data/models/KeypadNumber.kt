package com.vau.studio.iosstyle.idialer_phone.data.models

data class KeypadNumber(
    val number: String,
    val optionalString: List<String>? = listOf(),
) {}