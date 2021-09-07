package com.vau.studio.iosstyle.idialer_phone.data.models

const val INCOMING_CALL : String = "INCOMING"
const val OUTGOING_CALL : String = "OUTGOING"

data class CallHistory(
    val name: String? = "",
    val number: String? = "",
    val date: String? = "",
    val type: String? = INCOMING_CALL,
    val duration: String? = ""
)