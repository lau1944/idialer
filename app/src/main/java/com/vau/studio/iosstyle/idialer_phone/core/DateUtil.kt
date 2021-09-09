package com.vau.studio.iosstyle.idialer_phone.core

import android.annotation.SuppressLint
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    val DAYS_IN_WEEK = listOf(
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )

    /**
     * parse timestamp to date object
     */
    fun parseToDate(timestamp: Long): Date? {
        return try {
            Date(timestamp)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parse date to format 'yyyy/MM/dd
     */
    @SuppressLint("SimpleDateFormat")
    fun parseToDateFormat(date: Date?): String {
        if (date == null) return ""

        return SimpleDateFormat("yyyy/MM/dd").format(date)
    }

    /**
     * Parse data to time format
     */
    @SuppressLint("SimpleDateFormat")
    fun parseToTimeFormat(date: Date?): String {
        if (date == null) return ""

        return SimpleDateFormat("HH:mm").format(date)
    }

    /**
     * parse date to week day string
     */
    @SuppressLint("SimpleDateFormat")
    fun parseToWeek(date: Date): String? {
        return try {
            return SimpleDateFormat("E").format(date)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Compare correspond date to today
     * return a time dif with the unit of [dateType]
     */
    fun compareDateDif(targetDate: Date, dateType: DateType): Int {
        val targetCalendar = Calendar.getInstance().apply {
            time = targetDate
        }
        val todayCalendar = Calendar.getInstance()
        when (dateType) {
            DateType.Year -> {
                val targetYear = targetCalendar.get(Calendar.YEAR)
                val currentYear = todayCalendar.get(Calendar.YEAR)
                return currentYear - targetYear
            }
            DateType.Month -> {
                var difMonth = 0
                val difYear = compareDateDif(targetDate, DateType.Year)
                val targetMonth = targetCalendar.get(Calendar.MONTH)
                val currentMonth = todayCalendar.get(Calendar.MONTH)
                if (difYear >= 1)
                    difMonth = (difYear - 1) * 12
                else
                    return difMonth + currentMonth - targetMonth

                difMonth += if (currentMonth > targetMonth) {
                    12 + currentMonth - targetMonth
                } else {
                    12 - targetMonth + currentMonth
                }
                return difMonth
            }
            DateType.Week -> {
                val difMonth = compareDateDif(targetDate, DateType.Month)
                val targetWeek = targetCalendar.get(Calendar.WEEK_OF_MONTH)
                val currentWeek = todayCalendar.get(Calendar.WEEK_OF_MONTH)
                return if (difMonth >= 1) {
                    4 * (difMonth - 1) + currentWeek - targetWeek
                } else {
                    currentWeek - targetWeek
                }
            }
            DateType.Day -> {
                val difWeek = compareDateDif(targetDate, DateType.Week)
                val targetDay = targetCalendar.get(Calendar.DAY_OF_WEEK)
                val currentDay = todayCalendar.get(Calendar.DAY_OF_WEEK)
                return if (difWeek >= 1) {
                    (difWeek - 1) * 4 + currentDay - targetDay
                } else {
                    currentDay - targetDay
                }
            }
        }
    }

}

enum class DateType {
    Day, Week, Month, Year
}