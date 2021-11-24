package com.vau.studio.iosstyle.idialer_phone.data

import android.provider.CallLog
import android.provider.ContactsContract
import com.vau.studio.iosstyle.idialer_phone.data.models.ContactPageType

const val SHARED_PREFERENCE_KEY = "i_dialer"
const val NAV_SCREEN_KEY = "nav_screen"
const val DEFAULT_SCREEN_NAME = "keypad"

const val THEME_KEY = "theme"
const val DARK_THEME = 1
const val LIGHT_THEME = 0
const val REQUEST_CODE_FOR_DIALER = 12355

const val CONTACT_READ_PERMISSION = android.Manifest.permission.READ_CONTACTS
const val CONTACT_WRITE_PERMISSION = android.Manifest.permission.WRITE_CONTACTS
const val CALL_LOG_READ_PERMISSION = android.Manifest.permission.READ_CALL_LOG
const val CALL_LOG_WRITE_PERMISSION = android.Manifest.permission.WRITE_CALL_LOG

const val MISSED_CALL_TYPE = CallLog.Calls.MISSED_TYPE
const val OUTGOING_CALL_TYPE = CallLog.Calls.OUTGOING_TYPE
const val INCOMING_CALL_TYPE = CallLog.Calls.INCOMING_TYPE
const val ALL_CALL_TYPE = 1044439890

const val QUERY_LOG_BY_NUMBER     = "${CallLog.Calls.NUMBER}="
const val QUERY_CONTACT_BY_NUMBER = "${ContactsContract.CommonDataKinds.Phone.NUMBER}="
const val QUERY_CONTACT_BY_ID     = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID}="

