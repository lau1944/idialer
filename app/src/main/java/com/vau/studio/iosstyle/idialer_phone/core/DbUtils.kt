package com.vau.studio.iosstyle.idialer_phone.core

object DbUtils {
    fun isSuccess(operandNumber: Long) : Boolean {
        return operandNumber.toInt() == 0
    }
}