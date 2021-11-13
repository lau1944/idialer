package com.vau.studio.iosstyle.idialer_phone.core

object TimeUtils {

    /**
     * Parse timestamp to string format
     */
    fun parseToString(time: Long?) : String {
        if (time == null) return ""

        val buffer = StringBuffer()
        val secs = time * 1000
        val min = secs / 60
        val hours = min / 60

        return when {
            secs < 60 -> {
                buffer.append(secs)
                buffer.append(" Seconds ")
            }
            min < 60 -> {
                buffer.append(min)
                buffer.append(" Minutes ")
            }
            else -> {
                buffer.append(hours)
                buffer.append(" Hours ")
            }
        }.toString()

    }

}