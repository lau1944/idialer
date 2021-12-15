package com.vau.studio.iosstyle.idialer_phone.services

import android.os.Build
import android.telecom.ConnectionService
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
class IDialerConnectionService : ConnectionService() {
}