package com.vau.studio.iosstyle.idialer_phone.core

class DeviceUtil {

    companion object {
        @JvmField
        var info: DeviceInfo? = null

        @JvmStatic
        fun setInfo(deviceInfo: DeviceInfo) {
            if (info != null) return

            info = deviceInfo
        }
    }

}

data class DeviceInfo (val width: Int, val height: Int)