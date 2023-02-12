package com.trunnghieu.tplogisticsapplication.utils.helper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateTimeHelper {

    /**
     * Get time from milliseconds
     */
    fun getTimeFromMillis(millis: Long): String {
        val timeBuilder = StringBuilder()

        // Minutes
        val remainMinutes = TimeUnit.MILLISECONDS.toMinutes(millis).toInt()
        if (remainMinutes < 10) {
            timeBuilder.append(0).append(remainMinutes)
        } else {
            timeBuilder.append(remainMinutes)
        }

        timeBuilder.append(":")

        // Seconds
        val remainSeconds = TimeUnit.MILLISECONDS.toSeconds(millis).toInt()
        if (remainSeconds < 10) {
            timeBuilder.append(0).append(remainSeconds)
        } else {
            if (remainSeconds == 60) {
                timeBuilder.append("00")
            } else {
                timeBuilder.append(remainSeconds)
            }
        }

        return timeBuilder.toString()
    }

    /**
     * Get milliseconds from date
     */
    fun getMillisFromDate(currDate: String, dateFormat: String): Long {
        val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        val date = sdf.parse(currDate)
        date?.let {
            return it.time
        }
        return 0
    }

    /**
     * Convert current milliseconds to date
     */
    @JvmStatic
    fun currentMillisToDate(dateFormat: String): String {
        val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        return sdf.format(Date(System.currentTimeMillis()))
    }

    /**
     * Convert current milliseconds to time
     */
    @JvmStatic
    fun currentMillisToTime(timeFormat: String): String {
        val sdf = SimpleDateFormat(timeFormat, Locale.ENGLISH)
        return sdf.format(Date(System.currentTimeMillis()))
    }

    /**
     * Convert milliseconds to date
     */
    @JvmStatic
    fun millisToDate(millis: Long, dateFormat: String): String {
        val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        return sdf.format(Date(millis))
    }

    /**
     * Change current date to new date with difference date format
     */
    @JvmStatic
    fun changeFormat(inDate: String?, inDateFormat: String, outDateFormat: String): String {
        if (inDate.isNullOrEmpty()) return ""
        val inSdf = SimpleDateFormat(inDateFormat, Locale.ENGLISH)
        inSdf.parse(inDate)?.time?.let { inDateMillis ->
            return SimpleDateFormat(outDateFormat, Locale.ENGLISH).format(inDateMillis)
        }
        return inDate
    }

    /**
     * Get calendar from date
     */
    fun getCalendarFromDate(inDate: String, inDateFormat: String): Calendar {
        val inSdf = SimpleDateFormat(inDateFormat, Locale.ENGLISH)
        val inTimeMillis = inSdf.parse(inDate)?.time ?: 0
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = inTimeMillis
        return calendar
    }

    /**
     * Convert UTC date from server to local GMT date on phone
     */
    @JvmStatic
    fun utcToGmt(inDate: String, inDateFormat: String, outDateFormat: String): String {
        val localDate = Date()
        val utcSdf = SimpleDateFormat(inDateFormat, Locale.ENGLISH).apply {
            // Set time zone to UTC
            timeZone = TimeZone.getTimeZone("UTC")
        }
        var utcTimeMillis: Long = 0
        try {
            utcTimeMillis = utcSdf.parse(inDate)?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        localDate.time = utcTimeMillis

        return SimpleDateFormat(outDateFormat, Locale.ENGLISH).apply {
            // Set time zone to default
            timeZone = TimeZone.getDefault()
        }.format(localDate)
    }

    /**
     * Convert GMT local time to Server UTC0 time
     */
    @JvmStatic
    fun gmtToUtc(inDate: String, dateFormat: String): String {
        val utcDate = Date()
        val localSdf = SimpleDateFormat(dateFormat, Locale.ENGLISH).apply {
            // Set time zone to default
            timeZone = TimeZone.getDefault()
        }
        var localTimeMillis: Long = 0
        try {
            localTimeMillis = localSdf.parse(inDate)?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        utcDate.time = localTimeMillis
        return SimpleDateFormat(dateFormat, Locale.ENGLISH).apply {
            // Set time zone to UTC
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(utcDate)
    }
}
