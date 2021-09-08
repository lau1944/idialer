package com.vau.studio.iosstyle.idialer_phone.data.models

import android.provider.CallLog

const val INCOMING_CALL : Int = CallLog.Calls.INCOMING_TYPE
const val OUTGOING_CALL : Int = CallLog.Calls.OUTGOING_TYPE

data class CallHistory(
    val name: String? = "",
    val number: String? = "",
    val date: String? = "",
    val location: String? = "",
    val type: Int? = OUTGOING_CALL,
    val duration: String? = ""
)